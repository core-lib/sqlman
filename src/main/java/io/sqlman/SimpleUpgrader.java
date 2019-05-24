package io.sqlman;

import io.sqlman.dialect.MySQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;

/**
 * 基础执行器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:15
 */
public class SimpleUpgrader implements SqlUpgrader {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;
    private Connection connection;
    private SqlProvider provider = new SimpleProvider();
    private SqlDialect dialect = new MySQLDialect();
    private SqlConfig config = new SimpleConfig();

    @Override
    public void upgrade() throws Exception {
        try {
            // 建立连接
            setup();

            // 安装
            perform(new InstallTransaction());

            // 获取表锁
            perform(new LockTransaction());

            // 查询当前状态
            SqlVersion current = perform(new ExamineTransaction());

            // 执行升级脚本
            perform(new UpgradeTransaction(current));

            // 释放表锁
            perform(new UnlockTransaction());
        } catch (Exception e) {
            // 释放表锁
            perform(new UnlockTransaction());
            // 抛出异常阻止应用继续启动
            throw e;
        } finally {
            // 关闭连接
            close();
        }
    }

    private synchronized void setup() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            SqlIsolation isolation = config.getIsolation();
            if (isolation != SqlIsolation.DEFAULT) {
                connection.setTransactionIsolation(isolation.value);
            }
        }
    }

    private synchronized <T> T perform(SqlTransaction<T> transaction) throws SQLException {
        try {
            T result = transaction.execute(connection);
            connection.commit();
            return result;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw new SQLException(e);
        }
    }

    private synchronized void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private class InstallTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Initializing sqlman");
            dialect.install(connection, config);
            logger.info("Sqlman initialize completed");
            return null;
        }
    }

    private class LockTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            try {
                logger.info("Locking sqlman");
                dialect.lock(connection, config);
                logger.info("Sqlman locked");
                return null;
            } catch (SQLException e) {
                logger.error("Fail to acquire sqlman upgrade lock", e);
                throw e;
            }
        }
    }

    private class ExamineTransaction implements SqlTransaction<SqlVersion> {
        @Override
        public SqlVersion execute(Connection connection) throws SQLException {
            logger.info("Examining sqlman current version");
            SqlVersion current = dialect.examine(connection, config);
            logger.info("Sqlman current version is {}", current);
            return current;
        }
    }

    private class UpgradeTransaction implements SqlTransaction<Void> {
        private final SqlVersion current;

        UpgradeTransaction(SqlVersion current) {
            this.current = current;
        }

        @Override
        public Void execute(Connection connection) throws Exception {
            String version = current != null ? current.getVersion() : null;
            int ordinal = current == null ? 0 : current.getSuccess() ? current.getOrdinal() + 1 : current.getOrdinal();

            String dbType = dialect.type().value;
            Enumeration<SqlScript> scripts = version == null ? provider.acquire(dbType) : provider.acquire(dbType, version);
            while (scripts.hasMoreElements()) {
                SqlScript script = scripts.nextElement();
                int count = script.sqls();
                for (int index = ordinal; index < count; index++) {
                    int rowEffected = 0;
                    SQLException sqlException = null;
                    try {
                        rowEffected = perform(new ExecuteTransaction(script, index));
                    } catch (SQLException e) {
                        sqlException = e;
                        throw e;
                    } finally {
                        perform(new RecordTransaction(script, index, rowEffected, sqlException));
                    }
                }
                ordinal = 0;
            }

            return null;
        }
    }

    private class ExecuteTransaction implements SqlTransaction<Integer> {
        private final SqlScript script;
        private final int ordinal;

        ExecuteTransaction(SqlScript script, int ordinal) {
            this.script = script;
            this.ordinal = ordinal;
        }

        @Override
        public Integer execute(Connection connection) throws SQLException {
            String version = script.version() + "/" + ordinal;
            logger.info("Executing SQL script {}", version);
            int rowEffected = script.execute(connection, ordinal);
            logger.info("SQL script record completed");
            return rowEffected;
        }
    }

    private class RecordTransaction implements SqlTransaction<Void> {
        private final SqlScript script;
        private final int ordinal;
        private final int rowEffected;
        private final SQLException sqlException;

        RecordTransaction(SqlScript script, int ordinal, int rowEffected, SQLException sqlException) {
            this.script = script;
            this.ordinal = ordinal;
            this.rowEffected = rowEffected;
            this.sqlException = sqlException;
        }

        @Override
        public Void execute(Connection connection) throws SQLException {
            if (sqlException == null) {
                SqlVersion version = new SqlVersion();
                version.setVersion(script.version());
                version.setOrdinal(ordinal);
                version.setDescription(script.description());
                version.setSqlQuantity(script.sqls());
                version.setSuccess(true);
                version.setRowEffected(rowEffected);
                version.setErrorCode(0);
                version.setErrorState("");
                version.setErrorMessage("");
                version.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                dialect.record(connection, config, version);
            } else {
                SqlVersion version = new SqlVersion();
                version.setVersion(script.version());
                version.setOrdinal(ordinal);
                version.setDescription(script.description());
                version.setSqlQuantity(script.sqls());
                version.setSuccess(false);
                version.setRowEffected(0);
                version.setErrorCode(sqlException.getErrorCode());
                version.setErrorState(sqlException.getSQLState());
                version.setErrorMessage(sqlException.getMessage());
                version.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                dialect.record(connection, config, version);
            }
            return null;
        }
    }

    private class UnlockTransaction implements SqlTransaction<Void> {

        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Unlocking sqlman");
            dialect.unlock(connection, config);
            logger.info("Sqlman unlocked");
            return null;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SqlProvider getProvider() {
        return provider;
    }

    public void setProvider(SqlProvider provider) {
        this.provider = provider;
    }

    public SqlDialect getDialect() {
        return dialect;
    }

    public void setDialect(SqlDialect dialect) {
        this.dialect = dialect;
    }

    public SqlConfig getConfig() {
        return config;
    }

    public void setConfig(SqlConfig config) {
        this.config = config;
    }
}
