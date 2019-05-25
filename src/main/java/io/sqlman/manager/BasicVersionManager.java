package io.sqlman.manager;

import io.sqlman.SqlResource;
import io.sqlman.SqlScript;
import io.sqlman.SqlStatement;
import io.sqlman.SqlVersion;
import io.sqlman.provider.BasicScriptProvider;
import io.sqlman.provider.SqlScriptProvider;
import io.sqlman.resolver.BasicScriptResolver;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.support.MySQLDialectSupport;
import io.sqlman.support.SqlDialectSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class BasicVersionManager implements SqlVersionManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;
    private Connection jdbcConnection;
    private SqlIsolation trxIsolation = SqlIsolation.DEFAULT;
    private SqlScriptProvider scriptProvider = new BasicScriptProvider();
    private SqlScriptResolver scriptResolver = new BasicScriptResolver();
    private SqlDialectSupport dialectSupport = new MySQLDialectSupport();

    public BasicVersionManager() {
    }

    public BasicVersionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BasicVersionManager(DataSource dataSource, SqlIsolation trxIsolation, SqlScriptProvider scriptProvider, SqlScriptResolver scriptResolver, SqlDialectSupport dialectSupport) {
        this.dataSource = dataSource;
        this.trxIsolation = trxIsolation;
        this.scriptProvider = scriptProvider;
        this.scriptResolver = scriptResolver;
        this.dialectSupport = dialectSupport;
    }

    @Override
    public void upgrade() throws Exception {
        try {
            // 开始升级
            logger.info("Upgrading database");

            // 建立连接
            setup();

            // 获取升级锁
            perform(new LockTransaction());
            try {
                // 安装
                perform(new InstallTransaction());

                // 查询当前状态
                SqlVersion current = perform(new ExamineTransaction());

                // 执行升级脚本
                perform(new UpgradeTransaction(current));
            } finally {
                // 释放升级锁
                perform(new UnlockTransaction());
            }

            // 已经升级到最新
            logger.info("Database is up to date");
        } finally {
            // 关闭连接
            close();
        }
    }

    private synchronized void setup() throws SQLException {
        if (jdbcConnection == null) {
            jdbcConnection = dataSource.getConnection();
            jdbcConnection.setAutoCommit(false);
            if (trxIsolation != SqlIsolation.DEFAULT) {
                jdbcConnection.setTransactionIsolation(trxIsolation.value);
            }
        }
    }

    private synchronized <T> T perform(SqlTransaction<T> transaction) throws SQLException {
        try {
            T result = transaction.execute(jdbcConnection);
            jdbcConnection.commit();
            return result;
        } catch (SQLException e) {
            if (jdbcConnection != null) {
                jdbcConnection.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (jdbcConnection != null) {
                jdbcConnection.rollback();
            }
            throw new SQLException(e);
        }
    }

    private synchronized void close() throws SQLException {
        if (jdbcConnection != null) {
            jdbcConnection.close();
        }
    }

    /**
     * 事务操作
     *
     * @author Payne 646742615@qq.com
     * 2019/5/24 10:57
     */
    private interface SqlTransaction<T> {

        /**
         * 执行事务
         *
         * @param connection 连接
         * @return 事务执行结果
         * @throws Exception 事务执行异常
         */
        T execute(Connection connection) throws Exception;

    }

    private class InstallTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Initializing sqlman");
            dialectSupport.install(connection);
            logger.info("Sqlman initialize completed");
            return null;
        }
    }

    private class LockTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            try {
                logger.info("Locking sqlman");
                dialectSupport.lock(connection);
                logger.info("Sqlman locked");
                return null;
            } catch (SQLException e) {
                logger.error("Fail to acquire sqlman upgrade lock for " + e.getMessage(), e);
                throw e;
            }
        }
    }

    private class ExamineTransaction implements SqlTransaction<SqlVersion> {
        @Override
        public SqlVersion execute(Connection connection) throws SQLException {
            logger.info("Examining sqlman current version");
            SqlVersion current = dialectSupport.examine(connection);
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

            Enumeration<SqlResource> resources = current == null
                    ? scriptProvider.acquire()
                    : scriptProvider.acquire(version, ordinal < current.getSqlQuantity() - 1);

            while (resources.hasMoreElements()) {
                SqlResource resource = resources.nextElement();
                SqlScript script = scriptResolver.resolve(resource);
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
            logger.info("Executing SQL script: {}", script.name());
            SqlStatement statement = script.statement(ordinal);
            String sql = statement.statement();
            logger.info("Executing SQL statement: {}#{}\n{}", script.version(), ordinal, sql);
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rowEffected = stmt.executeUpdate();
            logger.info("SQL statement: {}#{} execute completed with {} rows effected", script.version(), ordinal, rowEffected);
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
                version.setName(script.name());
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
                dialectSupport.record(connection, version);
            } else {
                SqlVersion version = new SqlVersion();
                version.setName(script.name());
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
                dialectSupport.record(connection, version);
            }
            return null;
        }
    }

    private class UnlockTransaction implements SqlTransaction<Void> {

        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Unlocking sqlman");
            dialectSupport.unlock(connection);
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

    public SqlIsolation getTrxIsolation() {
        return trxIsolation;
    }

    public void setTrxIsolation(SqlIsolation trxIsolation) {
        this.trxIsolation = trxIsolation;
    }

    public SqlScriptProvider getScriptProvider() {
        return scriptProvider;
    }

    public void setScriptProvider(SqlScriptProvider scriptProvider) {
        this.scriptProvider = scriptProvider;
    }

    public SqlScriptResolver getScriptResolver() {
        return scriptResolver;
    }

    public void setScriptResolver(SqlScriptResolver scriptResolver) {
        this.scriptResolver = scriptResolver;
    }

    public SqlDialectSupport getDialectSupport() {
        return dialectSupport;
    }

    public void setDialectSupport(SqlDialectSupport dialectSupport) {
        this.dialectSupport = dialectSupport;
    }

}
