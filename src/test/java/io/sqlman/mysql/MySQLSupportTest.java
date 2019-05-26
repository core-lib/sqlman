package io.sqlman.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.manager.BasicVersionManager;
import io.sqlman.provider.BasicSourceProvider;
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
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sqlman?serverTimezone=GMT%2B8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        BasicVersionManager manager = new BasicVersionManager();
        manager.setDialectSupport(new MySQLDialectSupport());
        manager.setDataSource(dataSource);
        manager.setSourceProvider(new BasicSourceProvider("sqlman/MySQL/**/*.sql"));
        manager.upgrade();
    }

}
