package io.sqlman.core.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.core.dialect.OracleDialectSupport;
import io.sqlman.core.script.DruidScriptResolver;
import io.sqlman.core.source.ClasspathSourceProvider;
import io.sqlman.core.version.JdbcVersionManager;
import org.junit.Test;

/**
 * Oracle测试
 *
 * @author Payne 646742615@qq.com
 * 2019/5/26 11:20
 */
public class OracleSupportTest {

    @Test
    public void test() throws Exception {
        JdbcVersionManager manager = null;
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl("jdbc:oracle:thin:@//localhost:1521/sqlman");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            manager = new JdbcVersionManager(dataSource);
            manager.setDataSource(dataSource);
            manager.setDialectSupport(new OracleDialectSupport());
            manager.setScriptResolver(new DruidScriptResolver(JdbcUtils.ORACLE));
            manager.setSourceProvider(new ClasspathSourceProvider("sqlman/**/*.sql"));
            manager.upgrade();
        } finally {
            if (manager != null) {
                manager.remove();
            }
        }
    }

}
