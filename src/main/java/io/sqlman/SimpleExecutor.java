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
public class SimpleExecutor implements SqlExecutor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;
    private SqlProvider provider = new SimpleProvider();
    private SqlDialect dialect = new MySQLDialect();
    private SqlConfig config = new SimpleConfig();

    @Override
    public void execute() throws Exception {
        // 初始化
        initialize();
        // 查询状态
        SqlVersion current = retrieve();
        // 获取当前版本之后的所有升级脚本
        String version = current != null ? current.getVersion() : null;
        // 获取脚本下一个执行的SQL语句序好
        int ordinal = current == null
                ? 0
                : current.getSuccess()
                ? current.getOrdinal() + 1
                : current.getOrdinal();

        String dbType = dialect.type();
        Enumeration<SqlScript> scripts = version == null ? provider.acquire(dbType) : provider.acquire(dbType, version);
        // 按顺序执行脚本
        while (scripts.hasMoreElements()) {
            SqlScript script = scripts.nextElement();
            int sqls = script.sqls();
            for (int index = ordinal; index < sqls; index++) {
                execute(script, index);
            }
            ordinal = 0;
        }
        logger.info("Sqlman upgrade completed");
    }

    private void initialize() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            logger.info("Initializing sqlman");
            dialect.install(connection, config);
            logger.info("Sqlman initialize completed");
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Sqlman initialize failed", e);
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private SqlVersion retrieve() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            logger.info("Retrieving sqlman version");
            SqlVersion status = dialect.status(connection, config);
            logger.info("Sqlman version is {}", status);
            return status;
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Sqlman version retrieve failed", e);
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void execute(SqlScript script, int ordinal) throws Exception {
        boolean success = false;
        int rowEffected = 0;
        int errorCode = 0;
        String errorState = null;
        String errorMessage = null;
        Timestamp dateExecuted = new Timestamp(System.currentTimeMillis());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            String version = script.version() + "/" + ordinal;
            logger.info("Executing SQL script {}", version);
            rowEffected = script.execute(connection, ordinal);
            logger.info("SQL script execute completed");
            success = true;
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("SQL script execute failed", e);
            errorCode = e.getErrorCode();
            errorState = e.getSQLState();
            errorMessage = e.getMessage();
            throw e;
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            errorCode = -1;
            errorState = e.getMessage();
            errorMessage = e.getMessage();
        } finally {
            if (connection != null) {
                connection.close();
            }
            SqlVersion version = new SqlVersion();
            version.setVersion(script.version());
            version.setOrdinal(ordinal);
            version.setDescription(script.description());
            version.setSqlQuantity(script.sqls());
            version.setSuccess(success);
            version.setRowEffected(rowEffected);
            version.setErrorCode(errorCode);
            version.setErrorState(errorState);
            version.setErrorMessage(errorMessage);
            version.setTimeExecuted(dateExecuted);
            update(version);
        }
    }

    private void update(SqlVersion version) throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            logger.info("Updating sqlman to version {}", version);
            dialect.upgrade(connection, config, version);
            logger.info("Sqlman version update completed");
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Sqlman version update failed", e);
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
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
