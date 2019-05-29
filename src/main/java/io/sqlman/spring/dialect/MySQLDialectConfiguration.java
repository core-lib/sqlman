package io.sqlman.spring.dialect;

import io.sqlman.SqlDialectSupport;
import io.sqlman.dialect.MySQLDialectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * MySQL方言自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:31
 */
@Configuration
@EnableConfigurationProperties(MySQLDialectProperties.class)
@ConditionalOnClass(MySQLDialectSupport.class)
@ConditionalOnProperty(prefix = "sqlman.dialect", name = "type", havingValue = "MySQL", matchIfMissing = true)
public class MySQLDialectConfiguration {

    @Resource
    private MySQLDialectProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlDialectSupport.class)
    public MySQLDialectSupport sqlmanMySQLDialectSupport() {
        String table = properties.getTable();
        return new MySQLDialectSupport(table);
    }

}
