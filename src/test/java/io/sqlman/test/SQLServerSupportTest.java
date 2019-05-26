package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.manager.BasicVersionManager;
import io.sqlman.provider.BasicSourceProvider;
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
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlserver://182.254.171.152;database=ZT_MyRegentV3.0");
        dataSource.setUsername("SA");
        dataSource.setPassword("regentsoft!2019");
        BasicVersionManager manager = new BasicVersionManager();
        manager.setDialectSupport(new SQLServerDialectSupport());
        manager.setDataSource(dataSource);
        manager.setSourceProvider(new BasicSourceProvider("sqlman/**/*.sql"));
        manager.upgrade();
        manager.remove();
    }

}
