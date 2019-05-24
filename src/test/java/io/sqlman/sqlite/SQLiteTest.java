package io.sqlman.sqlite;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.SimpleProvider;
import io.sqlman.SimpleUpgrader;
import io.sqlman.dialect.SQLiteDialect;
import org.junit.Test;

/**
 * MySQL测试
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 13:17
 */
public class SQLiteTest {

    @Test
    public void test() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlite:target/SQLite.db?date_string_format=yyyy-MM-dd HH:mm:ss&date_class=TEXT&journal_mode=WAL");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        SimpleUpgrader upgrader = new SimpleUpgrader();
        upgrader.setDataSource(dataSource);
        upgrader.setDialect(new SQLiteDialect());
        upgrader.setProvider(new SimpleProvider("sqlman/SQLite/**/*.sql"));
        upgrader.upgrade();
    }

}
