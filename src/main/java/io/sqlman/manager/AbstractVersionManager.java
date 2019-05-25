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
    protected SqlSourceProvider scriptProvider = new BasicSourceProvider();
    protected SqlScriptResolver scriptResolver = new BasicScriptResolver();
    protected SqlDialectSupport dialectSupport = new MySQLDialectSupport();

    protected AbstractVersionManager() {
    }

    protected AbstractVersionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected AbstractVersionManager(DataSource dataSource, SqlIsolation trxIsolation, SqlSourceProvider scriptProvider, SqlScriptResolver scriptResolver, SqlDialectSupport dialectSupport) {
        this.dataSource = dataSource;
        this.trxIsolation = trxIsolation;
        this.scriptProvider = scriptProvider;
        this.scriptResolver = scriptResolver;
        this.dialectSupport = dialectSupport;
    }

    protected <T> T perform(SqlTransaction<T> transaction) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
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
    public Enumeration<SqlSource> acquire() throws MalformedNameException, DuplicatedVersionException, IOException {
        return scriptProvider.acquire();
    }

    @Override
    public Enumeration<SqlSource> acquire(String version, boolean included) throws MalformedNameException, DuplicatedVersionException, IOException {
        return scriptProvider.acquire(version, included);
    }

    @Override
    public SqlScript resolve(SqlSource resource) throws IncorrectSyntaxException, IOException {
        return scriptResolver.resolve(resource);
    }

    @Override
    public void create() throws SQLException {
        perform(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
                dialectSupport.create(connection);
                return null;
            }
        });
    }

    @Override
    public SqlVersion detect() throws SQLException {
        return perform(new SqlTransaction<SqlVersion>() {
            @Override
            public SqlVersion execute(Connection connection) throws Exception {
                return dialectSupport.detect(connection);
            }
        });
    }

    @Override
    public void update(final SqlVersion version) throws SQLException {
        perform(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
                dialectSupport.update(connection, version);
                return null;
            }
        });
    }

    @Override
    public void remove() throws SQLException {
        perform(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
                dialectSupport.remove(connection);
                return null;
            }
        });
    }

    @Override
    public void lockup() throws SQLException {
        perform(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
                dialectSupport.lockup(connection);
                return null;
            }
        });
    }

    @Override
    public void unlock() throws SQLException {
        perform(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
                dialectSupport.unlock(connection);
                return null;
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

    public SqlSourceProvider getScriptProvider() {
        return scriptProvider;
    }

    public void setScriptProvider(SqlSourceProvider scriptProvider) {
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
