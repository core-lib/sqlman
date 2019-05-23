package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.SimpleExecutor;
import org.junit.Test;

/**
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:27
 */
public class SimpleExecutorTests {

    @Test
    public void test() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://new-alpha.juniusoft.com:23306/new_mf_pro_bak_0408?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8");
        dataSource.setUsername("new_mf");
        dataSource.setPassword("new_mf.123");
        SimpleExecutor executor = new SimpleExecutor();
        executor.setDataSource(dataSource);
        executor.execute();
    }

}
