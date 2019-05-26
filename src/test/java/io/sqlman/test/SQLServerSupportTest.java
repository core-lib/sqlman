package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.manager.JdbcVersionManager;
import io.sqlman.provider.ClasspathSourceProvider;
import io.sqlman.resolver.BasicScriptResolver;
import io.sqlman.support.SQLServerDialectSupport;
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
            dataSource.setUrl("jdbc:sqlserver://182.254.171.152;database=ZT_MyRegentV3.0");
            dataSource.setUsername("SA");
            dataSource.setPassword("regentsoft!2019");
            manager = new JdbcVersionManager(dataSource);
            manager.setDataSource(dataSource);
            manager.setDialectSupport(new SQLServerDialectSupport());
            manager.setScriptResolver(new BasicScriptResolver(JdbcUtils.SQL_SERVER));
            manager.setSourceProvider(new ClasspathSourceProvider("sqlman/**/*.sql"));
            manager.upgrade();
        } finally {
            if (manager != null) {
                manager.remove();
            }
        }
    }

}
