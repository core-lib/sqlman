package io.sqlman.spring.dialect;

import io.sqlman.support.SQLiteDialectSupport;
import io.sqlman.support.SqlDialectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * SQLite方言自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:38
 */
@Configuration
@EnableConfigurationProperties(SQLiteDialectProperties.class)
@ConditionalOnClass(SQLiteDialectSupport.class)
@ConditionalOnProperty(prefix = "sqlman.dialect", name = "type", havingValue = "SQLite")
public class SQLiteDialectConfiguration {

    @Resource
    private SQLiteDialectProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlDialectSupport.class)
    public SQLiteDialectSupport sqlmanSQLiteDialectSupport() {
        String table = properties.getTable();
        return new SQLiteDialectSupport(table);
    }

}
