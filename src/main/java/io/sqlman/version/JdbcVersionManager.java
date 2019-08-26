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

    /**
     * 缺省隔离界别
     */
    protected JdbcIsolation defaultIsolation;

    /**
     * 缺省模式
     */
    protected JdbcMode defaultMode = JdbcMode.DANGER;

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

    public JdbcVersionManager(
            DataSource dataSource,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport,
            SqlLoggerSupplier loggerSupplier,
            JdbcIsolation defaultIsolation,
            JdbcMode defaultMode
    ) {
        super(dataSource, sourceProvider, scriptResolver, dialectSupport, loggerSupplier);
        this.defaultIsolation = defaultIsolation;
        this.defaultMode = defaultMode;
    }

    @Override
    public void upgrade() throws SQLException {
        SqlLogger logger = logger(this.getClass());

        logger.info("Schema locking");
        lockup();
        logger.info("Schema locked");
        try {
            logger.info("Creating schema version table");
            create();

            logger.info("Detecting schema current version");
            SqlVersion current = detect();
            logger.info("Schema current version is {}", current);

            String version = current != null ? current.getVersion() : null;
            int ordinal = current != null ? current.getSuccess() ? current.getOrdinal() + 1 : current.getOrdinal() : 1;
            boolean included = current == null || !current.getSuccess() || ordinal < current.getSqlQuantity();
            Enumeration<SqlSource> sources = current != null ? acquire(version, included) : acquire();
            if (!included) {
                ordinal = 1;
            }

            if (sources.hasMoreElements()) {
                logger.info("Schema upgrading");
            }

            while (sources.hasMoreElements()) {
                SqlSource source = sources.nextElement();
                SqlScript script = resolve(source);
                if (ordinal == 1) {
                    upgrade(script);
                } else {
                    int sqls = script.sqls();
                    for (int odn = ordinal; odn <= sqls; odn++) {
                        upgrade(script, odn);
                    }
                    ordinal = 1;
                }
            }

            logger.info("Schema is up to date");
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            logger.info("Schema unlocking");
            unlock();
            logger.info("Schema unlocked");
        }
    }

    @Override
    public void upgrade(final SqlScript script) throws SQLException {
        SqlLogger logger = logger(this.getClass());

        Set<String> instructions = script.instructions();
        boolean atomic = instructions != null && instructions.contains(INSTRUCTION_ATOMIC);

        // 原子执行
        if (atomic) {
            logger.info("Executing script {} atomically", script.name());
            perform(new JdbcAction() {
                @Override
                public void perform(Connection connection) throws SQLException {
                    int sqls = script.sqls();
                    for (int ordinal = 1; ordinal <= sqls; ordinal++) {
                        upgrade(script, ordinal);
                    }
                }
            });
        }
        // 逐条执行
        else {
            logger.info("Executing script {} non-atomically", script.name());
            int sqls = script.sqls();
            for (int ordinal = 1; ordinal <= sqls; ordinal++) {
                upgrade(script, ordinal);
            }
        }
    }

    @Override
    public void upgrade(final SqlScript script, final int ordinal) throws SQLException {
        final SqlLogger logger = logger(this.getClass());

        Integer rowEffected = null;
        SQLException sqlException = null;
        try {
            rowEffected = execute(new JdbcTransaction<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    try {
                        Set<String> instructions = script.instructions();
                        JdbcIsolation isolation = JdbcIsolation.valueOf(instructions);
                        if (isolation == null) {
                            isolation = defaultIsolation;
                        }
                        if (isolation != null && connection.getTransactionIsolation() != isolation.level) {
                            connection.setTransactionIsolation(isolation.level);
                        }

                        JdbcMode mode = JdbcMode.valueOf(instructions);
                        if (mode == null) {
                            mode = defaultMode;
                        }
                        if (mode == null) {
                            mode = JdbcMode.DANGER;
                        }
                        if (mode == JdbcMode.SAFETY) {
                            dialectSupport.backup(connection, script, ordinal);
                        }

                        SqlSentence sentence = script.sentence(ordinal);
                        String sql = sentence.value();

                        logger.info(
                                "Executing sentence {}/{} of script version {} under {} transaction isolation level : {}",
                                ordinal,
                                script.sqls(),
                                script.version(),
                                isolation != null ? isolation.name : "default",
                                sql.replaceAll("\\s+", " ")
                        );

                        PreparedStatement statement = connection.prepareStatement(sql);
                        int rows = statement.executeUpdate();

                        logger.info("Execution completed with {} rows effected", rows);

                        return rows;
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

    public JdbcIsolation getDefaultIsolation() {
        return defaultIsolation;
    }

    public void setDefaultIsolation(JdbcIsolation defaultIsolation) {
        this.defaultIsolation = defaultIsolation;
    }

    public JdbcMode getDefaultMode() {
        return defaultMode;
    }

    public void setDefaultMode(JdbcMode defaultMode) {
        this.defaultMode = defaultMode;
    }
}
