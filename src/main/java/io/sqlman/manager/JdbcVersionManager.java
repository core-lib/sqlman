package io.sqlman.manager;

import io.sqlman.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;

/**
 * 基础SQL版本管理器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:15
 */
public class JdbcVersionManager extends AbstractVersionManager implements SqlVersionManager {

    protected JdbcVersionManager() {
        super();
    }

    public JdbcVersionManager(DataSource dataSource) {
        super(dataSource);
    }

    public JdbcVersionManager(
            DataSource dataSource,
            JdbcIsolation jdbcIsolation,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport,
            SqlLoggerSupplier loggerSupplier
    ) {
        super(dataSource, jdbcIsolation, sourceProvider, scriptResolver, dialectSupport, loggerSupplier);
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
    public void upgrade(SqlScript script) throws SQLException {
        int sqls = script.sqls();
        for (int ordinal = 0; ordinal < sqls; ordinal++) {
            upgrade(script, ordinal);
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
