package io.sqlman.manager;

import io.sqlman.*;
import io.sqlman.provider.ClasspathScriptProvider;
import io.sqlman.provider.SqlScriptProvider;
import io.sqlman.resolver.DruidScriptResolver;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.supporter.MySQLDialectSupporter;
import io.sqlman.supporter.SqlDialectSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;

/**
 * 基础执行器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:15
 */
public class StandardVersionManager implements SqlVersionManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;
    private Connection jdbcConnection;
    private SqlIsolation transactionIsolation = SqlIsolation.DEFAULT;
    private SqlScriptProvider scriptProvider = new ClasspathScriptProvider();
    private SqlScriptResolver scriptResolver = new DruidScriptResolver();
    private SqlDialectSupporter dialectSupporter = new MySQLDialectSupporter();
    private SqlConfig tableConfig = new SqlConfig();

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
            if (transactionIsolation != SqlIsolation.DEFAULT) {
                jdbcConnection.setTransactionIsolation(transactionIsolation.value);
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

    private class InstallTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Initializing sqlman");
            dialectSupporter.install(connection, tableConfig);
            logger.info("Sqlman initialize completed");
            return null;
        }
    }

    private class LockTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            try {
                logger.info("Locking sqlman");
                dialectSupporter.lock(connection, tableConfig);
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
            SqlVersion current = dialectSupporter.examine(connection, tableConfig);
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
            String version = script.version() + "/" + ordinal;
            logger.info("Executing SQL script {}", version);
            SqlStatement statement = script.statement(ordinal);
            String sql = statement.statement();
            logger.info("{}", sql);
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rowEffected = stmt.executeUpdate();
            logger.info("SQL script execute completed with {} rows effected", rowEffected);
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
                dialectSupporter.record(connection, tableConfig, version);
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
                dialectSupporter.record(connection, tableConfig, version);
            }
            return null;
        }
    }

    private class UnlockTransaction implements SqlTransaction<Void> {

        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Unlocking sqlman");
            dialectSupporter.unlock(connection, tableConfig);
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

    public SqlIsolation getTransactionIsolation() {
        return transactionIsolation;
    }

    public void setTransactionIsolation(SqlIsolation transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
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

    public SqlDialectSupporter getDialectSupporter() {
        return dialectSupporter;
    }

    public void setDialectSupporter(SqlDialectSupporter dialectSupporter) {
        this.dialectSupporter = dialectSupporter;
    }

    public SqlConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(SqlConfig tableConfig) {
        this.tableConfig = tableConfig;
    }
}