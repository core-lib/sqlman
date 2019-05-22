package io.sqlman.executor;

import io.sqlman.*;
import io.sqlman.config.SimpleConfig;
import io.sqlman.dialect.MySQLDialect;
import io.sqlman.provider.FileProvider;

import javax.sql.DataSource;
import java.sql.Connection;

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
        Connection connection = dataSource.getConnection();
        // 安装
        dialect.install(connection, config);
        // 查询状态
        SqlVersion current = dialect.status(connection, config);
        
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
