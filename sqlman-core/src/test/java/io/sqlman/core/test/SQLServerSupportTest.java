package io.sqlman.core.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.core.dialect.SQLServerDialectSupport;
import io.sqlman.core.script.DruidScriptResolver;
import io.sqlman.core.source.ClasspathSourceProvider;
import io.sqlman.core.version.JdbcVersionManager;
import org.junit.Test;

/**
 * SQLServer测试
 *
 * @author Payne 646742615@qq.com
 * 2019/5/26 9:28
 */
public class SQLServerSupportTest {

    @Test
    public void test() throws Exception {
        JdbcVersionManager manager = null;
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl("jdbc:sqlserver://localhost;database=sqlman");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            manager = new JdbcVersionManager(dataSource);
            manager.setDataSource(dataSource);
            manager.setDialectSupport(new SQLServerDialectSupport());
            manager.setScriptResolver(new DruidScriptResolver(JdbcUtils.SQL_SERVER));
            manager.setSourceProvider(new ClasspathSourceProvider("sqlman/**/*.sql"));
            manager.upgrade();
        } finally {
            if (manager != null) {
                manager.remove();
            }
        }
    }

}
