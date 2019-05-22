package io.sqlman.executor;

import io.sqlman.*;
import io.sqlman.config.SimpleConfig;
import io.sqlman.dialect.MySQLDialect;
import io.sqlman.provider.FileProvider;

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
public class BasicExecutor implements SqlExecutor {
    private DataSource dataSource;
    private SqlProvider provider = new FileProvider();
    private SqlDialect dialect = new MySQLDialect();
    private SqlConfig config = new SimpleConfig();

    @Override
    public void execute() throws Exception {
        // 安装
        install();
        // 查询状态
        SqlVersion current = status();
        // 获取当前版本之后的所有升级脚本
        String version = current != null ? current.getVersion() : null;
        // 获取脚本下一个执行的SQL语句序好
        int ordinal = current == null
                ? 0
                : current.getSuccess()
                ? current.getOrdinal() + 1
                : current.getOrdinal();

        Enumeration<SqlScript> scripts = version == null ? provider.acquire() : provider.acquire(version);
        // 按顺序执行脚本
        while (scripts.hasMoreElements()) {
            SqlScript script = scripts.nextElement();
            int sqls = script.sqls();
            for (int index = ordinal; index < sqls; index++) {
                upgrade(script, index);
            }
        }
    }

    private void install() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            dialect.install(connection, config);
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private SqlVersion status() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return dialect.status(connection, config);
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void upgrade(SqlScript script, int ordinal) throws Exception {
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
            rowEffected = script.execute(connection, ordinal);
            success = true;
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
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
            record(version);
        }
    }

    private void record(SqlVersion version) throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            dialect.upgrade(connection, config, version);
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
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
