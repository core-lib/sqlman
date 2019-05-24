package io.sqlman.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.SimpleProvider;
import io.sqlman.SimpleUpgrader;
import org.junit.Test;

/**
 * MySQL测试
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 13:17
 */
public class MySQLTest {

    @Test
    public void test() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sqlman?serverTimezone=GMT%2B8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        SimpleUpgrader upgrader = new SimpleUpgrader();
        upgrader.setDataSource(dataSource);
        upgrader.setProvider(new SimpleProvider("sqlman/MySQL/**/*.sql"));
        upgrader.upgrade();
    }

}
