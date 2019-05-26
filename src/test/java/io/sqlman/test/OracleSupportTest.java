package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.manager.BasicVersionManager;
import io.sqlman.provider.BasicSourceProvider;
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
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@//203.195.172.217:1521/regent");
        dataSource.setUsername("regent");
        dataSource.setPassword("regent2019");
        BasicVersionManager manager = new BasicVersionManager();
        manager.setDialectSupport(new OracleDialectSupport());
        manager.setDataSource(dataSource);
        manager.setSourceProvider(new BasicSourceProvider("sqlman/**/*.sql"));
        manager.upgrade();
        manager.remove();
    }

}
