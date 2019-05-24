package io.sqlman.sqlite;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.manager.StandardVersionManager;
import io.sqlman.provider.StandardScriptProvider;
import io.sqlman.support.SQLiteDialectSupport;
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
        StandardVersionManager upgrader = new StandardVersionManager();
        upgrader.setDataSource(dataSource);
        upgrader.setDialectSupport(new SQLiteDialectSupport());
        upgrader.setScriptProvider(new StandardScriptProvider("sqlman/SQLite/**/*.sql"));
        upgrader.upgrade();
    }

}
