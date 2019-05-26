package io.sqlman.manager;

import io.sqlman.SqlScript;
import io.sqlman.SqlSource;
import io.sqlman.SqlVersion;
import io.sqlman.provider.BasicSourceProvider;
import io.sqlman.provider.DuplicatedVersionException;
import io.sqlman.provider.MalformedNameException;
import io.sqlman.provider.SqlSourceProvider;
import io.sqlman.resolver.BasicScriptResolver;
import io.sqlman.resolver.IncorrectSyntaxException;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.support.MySQLDialectSupport;
import io.sqlman.support.SqlDialectSupport;

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
    protected SqlIsolation trxIsolation = SqlIsolation.DEFAULT;
    protected SqlSourceProvider sourceProvider = new BasicSourceProvider();
    protected SqlScriptResolver scriptResolver = new BasicScriptResolver();
    protected SqlDialectSupport dialectSupport = new MySQLDialectSupport();

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
            SqlIsolation trxIsolation,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport
    ) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource must not be null");
        }
        if (trxIsolation == null) {
            throw new IllegalArgumentException("trxIsolation must not be null");
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
        this.dataSource = dataSource;
        this.trxIsolation = trxIsolation;
        this.sourceProvider = sourceProvider;
        this.scriptResolver = scriptResolver;
        this.dialectSupport = dialectSupport;
    }

    @Override
    public <T> T execute(SqlTransaction<T> transaction) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            if (trxIsolation != null && trxIsolation != SqlIsolation.DEFAULT) {
                connection.setTransactionIsolation(trxIsolation.value);
            }
            T result = transaction.execute(connection);
            connection.commit();
            return result;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } catch (Throwable e) {
            if (connection != null) {
                connection.rollback();
            }
            throw new SQLException(e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void perform(final SqlAction action) throws SQLException {
        execute(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
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
        perform(new SqlAction() {
            @Override
            public void perform(Connection connection) throws Exception {
                dialectSupport.create(connection);
            }
        });
    }

    @Override
    public SqlVersion detect() throws SQLException {
        return execute(new SqlTransaction<SqlVersion>() {
            @Override
            public SqlVersion execute(Connection connection) throws Exception {
                return dialectSupport.detect(connection);
            }
        });
    }

    @Override
    public void update(final SqlVersion version) throws SQLException {
        perform(new SqlAction() {
            @Override
            public void perform(Connection connection) throws Exception {
                dialectSupport.update(connection, version);
            }
        });
    }

    @Override
    public void remove() throws SQLException {
        perform(new SqlAction() {
            @Override
            public void perform(Connection connection) throws Exception {
                dialectSupport.remove(connection);
            }
        });
    }

    @Override
    public void lockup() throws SQLException {
        perform(new SqlAction() {
            @Override
            public void perform(Connection connection) throws Exception {
                dialectSupport.lockup(connection);
            }
        });
    }

    @Override
    public void unlock() throws SQLException {
        perform(new SqlAction() {
            @Override
            public void perform(Connection connection) throws Exception {
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

    public SqlIsolation getTrxIsolation() {
        return trxIsolation;
    }

    public void setTrxIsolation(SqlIsolation trxIsolation) {
        this.trxIsolation = trxIsolation;
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
}
