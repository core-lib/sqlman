package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.dialect.SQLiteDialectSupport;
import io.sqlman.script.DruidScriptResolver;
import io.sqlman.source.ClasspathSourceProvider;
import io.sqlman.version.JdbcVersionManager;
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
        JdbcVersionManager manager = null;
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl("jdbc:sqlite:target/sqlman.db?date_string_format=yyyy-MM-dd HH:mm:ss&date_class=TEXT&journal_mode=WAL");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            manager = new JdbcVersionManager(dataSource);
            manager.setDataSource(dataSource);
            manager.setDialectSupport(new SQLiteDialectSupport());
            manager.setScriptResolver(new DruidScriptResolver(JdbcUtils.SQLITE));
            manager.setSourceProvider(new ClasspathSourceProvider("sqlman/**/*.sql"));
            manager.upgrade();
        } finally {
            if (manager != null) {
                manager.remove();
            }
        }
    }

}
