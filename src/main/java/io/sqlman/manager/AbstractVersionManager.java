package io.sqlman.manager;

import io.sqlman.*;
import io.sqlman.exception.DuplicatedVersionException;
import io.sqlman.exception.IncorrectSyntaxException;
import io.sqlman.exception.MalformedNameException;
import io.sqlman.logger.Slf4jLoggerSupplier;
import io.sqlman.provider.ClasspathSourceProvider;
import io.sqlman.resolver.DruidScriptResolver;
import io.sqlman.support.MySQLDialectSupport;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * 抽象的数据库版本管理器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 22:24
 */
public abstract class AbstractVersionManager implements SqlVersionManager {
    protected DataSource dataSource;
    protected JdbcIsolation jdbcIsolation = JdbcIsolation.DEFAULT;
    protected SqlSourceProvider sourceProvider = new ClasspathSourceProvider();
    protected SqlScriptResolver scriptResolver = new DruidScriptResolver();
    protected SqlDialectSupport dialectSupport = new MySQLDialectSupport();
    protected SqlLoggerSupplier loggerSupplier = new Slf4jLoggerSupplier();

    protected AbstractVersionManager() {
    }

    protected AbstractVersionManager(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource must not be null");
        }
        this.dataSource = dataSource;
    }

    protected AbstractVersionManager(
            DataSource dataSource,
            JdbcIsolation jdbcIsolation,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport,
            SqlLoggerSupplier loggerSupplier
    ) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource must not be null");
        }
        if (jdbcIsolation == null) {
            throw new IllegalArgumentException("jdbcIsolation must not be null");
        }
        if (sourceProvider == null) {
            throw new IllegalArgumentException("sourceProvider must not be null");
        }
        if (scriptResolver == null) {
            throw new IllegalArgumentException("scriptResolver must not be null");
        }
        if (dialectSupport == null) {
            throw new IllegalArgumentException("dialectSupport must not be null");
        }
        if (loggerSupplier == null) {
            throw new IllegalArgumentException("loggerSupplier must not be null");
        }
        this.dataSource = dataSource;
        this.jdbcIsolation = jdbcIsolation;
        this.sourceProvider = sourceProvider;
        this.scriptResolver = scriptResolver;
        this.dialectSupport = dialectSupport;
        this.loggerSupplier = loggerSupplier;
    }

    protected <T> T execute(JdbcTransaction<T> transaction) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            if (jdbcIsolation != null && jdbcIsolation != JdbcIsolation.DEFAULT) {
                connection.setTransactionIsolation(jdbcIsolation.value);
            }
            T result = transaction.execute(connection);
            connection.commit();
            return result;
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected void perform(final JdbcAction action) throws SQLException {
        execute(new JdbcTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws SQLException {
                action.perform(connection);
                return null;
            }
        });
    }

    @Override
    public Enumeration<SqlSource> acquire() throws MalformedNameException, DuplicatedVersionException, IOException {
        return sourceProvider.acquire();
    }

    @Override
    public Enumeration<SqlSource> acquire(String version, boolean included) throws MalformedNameException, DuplicatedVersionException, IOException {
        return sourceProvider.acquire(version, included);
    }

    @Override
    public SqlScript resolve(SqlSource resource) throws IncorrectSyntaxException, IOException {
        return scriptResolver.resolve(resource);
    }

    @Override
    public void create() throws SQLException {
        perform(new JdbcAction() {
            @Override
            public void perform(Connection connection) throws SQLException {
                dialectSupport.create(connection);
            }
        });
    }

    @Override
    public SqlVersion detect() throws SQLException {
        return execute(new JdbcTransaction<SqlVersion>() {
            @Override
            public SqlVersion execute(Connection connection) throws SQLException {
                return dialectSupport.detect(connection);
            }
        });
    }

    @Override
    public void update(final SqlVersion version) throws SQLException {
        perform(new JdbcAction() {
            @Override
            public void perform(Connection connection) throws SQLException {
                dialectSupport.update(connection, version);
            }
        });
    }

    @Override
    public void remove() throws SQLException {
        perform(new JdbcAction() {
            @Override
            public void perform(Connection connection) throws SQLException {
                dialectSupport.remove(connection);
            }
        });
    }

    @Override
    public void lockup() throws SQLException {
        perform(new JdbcAction() {
            @Override
            public void perform(Connection connection) throws SQLException {
                dialectSupport.lockup(connection);
            }
        });
    }

    @Override
    public void unlock() throws SQLException {
        perform(new JdbcAction() {
            @Override
            public void perform(Connection connection) throws SQLException {
                dialectSupport.unlock(connection);
            }
        });
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcIsolation getJdbcIsolation() {
        return jdbcIsolation;
    }

    public void setJdbcIsolation(JdbcIsolation jdbcIsolation) {
        this.jdbcIsolation = jdbcIsolation;
    }

    public SqlSourceProvider getSourceProvider() {
        return sourceProvider;
    }

    public void setSourceProvider(SqlSourceProvider sourceProvider) {
        this.sourceProvider = sourceProvider;
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

    public SqlLoggerSupplier getLoggerSupplier() {
        return loggerSupplier;
    }

    public void setLoggerSupplier(SqlLoggerSupplier loggerSupplier) {
        this.loggerSupplier = loggerSupplier;
    }
}
