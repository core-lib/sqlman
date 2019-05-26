package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.manager.BasicVersionManager;
import io.sqlman.provider.BasicSourceProvider;
import io.sqlman.resolver.BasicScriptResolver;
import io.sqlman.support.MySQLDialectSupport;
import org.junit.Test;

/**
 * MySQL测试
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 13:17
 */
public class MySQLSupportTest {

    @Test
    public void test() throws Exception {
        BasicVersionManager manager = null;
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/sqlman?serverTimezone=GMT%2B8&useSSL=false");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            manager = new BasicVersionManager(dataSource);
            manager.setDataSource(dataSource);
            manager.setDialectSupport(new MySQLDialectSupport());
            manager.setScriptResolver(new BasicScriptResolver(JdbcUtils.MYSQL));
            manager.setSourceProvider(new BasicSourceProvider("sqlman/**/*.sql"));
            manager.upgrade();
        } finally {
            if (manager != null) {
                manager.remove();
            }
        }
    }

}
