package io.sqlman.spring;

import io.sqlman.*;
import io.sqlman.manager.JdbcVersionManager;
import io.sqlman.provider.ClasspathSourceProvider;
import io.sqlman.resolver.DruidScriptResolver;
import io.sqlman.strategy.StandardNamingStrategy;
import io.sqlman.support.MySQLDialectSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Sqlman测试应用
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:59
 */
@SpringBootApplication
public class SqlmanTestApplication {

    public static void main(String... args) {
        SpringApplication.run(SqlmanTestApplication.class);
    }

    @Bean
    public SqlNamingStrategy sqlNamingStrategy() {
        return new StandardNamingStrategy();
    }

    @Bean
    public SqlSourceProvider sqlSourceProvider() {
        return new ClasspathSourceProvider();
    }

    @Bean
    public SqlScriptResolver sqlScriptResolver() {
        return new DruidScriptResolver();
    }

    @Bean
    public SqlDialectSupport sqlDialectSupport() {
        return new MySQLDialectSupport();
    }

    @Bean
    public SqlVersionManager sqlVersionManager(ApplicationContext applicationContext) throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        JdbcVersionManager jdbcVersionManager = new JdbcVersionManager(dataSource);
        jdbcVersionManager.upgrade();
        return jdbcVersionManager;
    }

}
