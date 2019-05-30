package io.sqlman.version;

import io.sqlman.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Set;

/**
 * 基础SQL版本管理器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:15
 */
public class JdbcVersionManager extends AbstractVersionManager implements SqlVersionManager, JdbcInstruction {
    protected JdbcVersionManager() {
        super();
    }

    public JdbcVersionManager(DataSource dataSource) {
        super(dataSource);
    }

    public JdbcVersionManager(
            DataSource dataSource,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport,
            SqlLoggerSupplier loggerSupplier
    ) {
        super(dataSource, sourceProvider, scriptResolver, dialectSupport, loggerSupplier);
    }

    @Override
    public void upgrade() throws SQLException {
        lockup();
        try {
            create();

            SqlVersion current = detect();

            String version = current != null ? current.getVersion() : null;
            int ordinal = current != null ? current.getSuccess() ? current.getOrdinal() + 1 : current.getOrdinal() : 0;
            boolean included = current == null || !current.getSuccess() || ordinal < current.getSqlQuantity() - 1;
            Enumeration<SqlSource> sources = current != null ? acquire(version, included) : acquire();

            while (sources.hasMoreElements()) {
                SqlSource source = sources.nextElement();
                SqlScript script = resolve(source);
                if (ordinal == 0) {
                    upgrade(script);
                } else {
                    int sqls = script.sqls();
                    for (int index = ordinal; index < sqls; index++) {
                        upgrade(script, index);
                    }
                    ordinal = 0;
                }
            }

        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            unlock();
        }
    }

    @Override
    public void upgrade(final SqlScript script) throws SQLException {
        Set<String> instructions = script.instructions();
        boolean atomic = instructions != null && instructions.contains(INSTRUCTION_ATOMIC);

        // 原子执行
        if (atomic) {
            perform(new JdbcAction() {
                @Override
                public void perform(Connection connection) throws SQLException {
                    int sqls = script.sqls();
                    for (int ordinal = 0; ordinal < sqls; ordinal++) {
                        upgrade(script, ordinal);
                    }
                }
            });
        }
        // 逐条执行
        else {
            int sqls = script.sqls();
            for (int ordinal = 0; ordinal < sqls; ordinal++) {
                upgrade(script, ordinal);
            }
        }
    }

    @Override
    public void upgrade(final SqlScript script, final int ordinal) throws SQLException {
        Integer rowEffected = null;
        SQLException sqlException = null;
        try {
            rowEffected = execute(new JdbcTransaction<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    try {
                        Set<String> instructions = script.instructions();
                        if (instructions != null && instructions.contains(INSTRUCTION_SERIALIZABLE)) {
                            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                        } else if (instructions != null && instructions.contains(INSTRUCTION_REPEATABLE_READ)) {
                            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                        } else if (instructions != null && instructions.contains(INSTRUCTION_READ_COMMITTED)) {
                            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        } else if (instructions != null && instructions.contains(INSTRUCTION_READ_UNCOMMITTED)) {
                            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }

                        SqlSentence sentence = script.sentence(ordinal);
                        String sql = sentence.value();
                        PreparedStatement statement = connection.prepareStatement(sql);
                        return statement.executeUpdate();
                    } catch (SQLException ex) {
                        throw ex;
                    } catch (Exception ex) {
                        String state = ex.getMessage() == null || ex.getMessage().isEmpty() ? "Unknown error" : ex.getMessage();
                        throw new SQLException(state, state, -1, ex);
                    }
                }
            });
        } catch (SQLException ex) {
            sqlException = ex;
            throw sqlException;
        } finally {
            SqlVersion version = new SqlVersion();
            version.setName(SqlUtils.ifEmpty(script.name(), "NO NAME"));
            version.setVersion(script.version());
            version.setOrdinal(ordinal);
            version.setDescription(SqlUtils.ifEmpty(script.description(), "NO DESCRIPTION"));
            version.setSqlQuantity(script.sqls());
            version.setSuccess(sqlException == null);
            version.setRowEffected(rowEffected == null ? 0 : rowEffected);
            version.setErrorCode(sqlException == null ? 0 : sqlException.getErrorCode());
            version.setErrorState(sqlException == null ? "OK" : SqlUtils.ifEmpty(sqlException.getSQLState(), "NO STATE"));
            version.setErrorMessage(sqlException == null ? "OK" : SqlUtils.ifEmpty(sqlException.getMessage(), "NO MESSAGE"));
            version.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
            update(version);
        }
    }

}
