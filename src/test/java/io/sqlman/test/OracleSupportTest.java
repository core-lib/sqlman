package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.manager.BasicVersionManager;
import io.sqlman.provider.BasicSourceProvider;
import io.sqlman.resolver.BasicScriptResolver;
import io.sqlman.support.OracleDialectSupport;
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
        BasicVersionManager manager = null;
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl("jdbc:oracle:thin:@//203.195.172.217:1521/regent");
            dataSource.setUsername("regent");
            dataSource.setPassword("regent2019");
            manager = new BasicVersionManager(dataSource);
            manager.setDataSource(dataSource);
            manager.setDialectSupport(new OracleDialectSupport());
            manager.setScriptResolver(new BasicScriptResolver(JdbcUtils.ORACLE));
            manager.setSourceProvider(new BasicSourceProvider("sqlman/**/*.sql"));
            manager.upgrade();
        } finally {
            if (manager != null) {
                manager.remove();
            }
        }
    }

}
