package io.sqlman.test;

import com.alibaba.druid.pool.DruidDataSource;
import io.sqlman.executor.BasicExecutor;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.StreamProvider;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:27
 */
public class BasicExecutorTests {

    @Test
    public void test() throws Exception {
        InputStream in = this.getClass().getResourceAsStream("/sqlman/v5.2.0-仓库平均成本价.sql");
        CCJSqlParser parser = new CCJSqlParser(new StreamProvider(in));
        parser.Statements();

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://new-alpha.juniusoft.com:23306/new_mf_pro_bak_0408?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8");
        dataSource.setUsername("new_mf");
        dataSource.setPassword("new_mf.123");
        BasicExecutor executor = new BasicExecutor();
        executor.setDataSource(dataSource);
        executor.execute();
    }

}
