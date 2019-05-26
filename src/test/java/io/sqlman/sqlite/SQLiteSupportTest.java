package io.sqlman.sqlite;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.manager.BasicVersionManager;
import io.sqlman.provider.BasicSourceProvider;
import io.sqlman.support.SQLiteDialectSupport;
import org.junit.Test;

/**
 * MySQL测试
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 13:17
 */
public class SQLiteSupportTest {

    @Test
    public void test() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlite:target/SQLite.db?date_string_format=yyyy-MM-dd HH:mm:ss&date_class=TEXT&journal_mode=WAL");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        BasicVersionManager manager = new BasicVersionManager();
        manager.setDataSource(dataSource);
        manager.setDialectSupport(new SQLiteDialectSupport());
        manager.setSourceProvider(new BasicSourceProvider("sqlman/SQLite/**/*.sql"));
        manager.upgrade();
    }

}
