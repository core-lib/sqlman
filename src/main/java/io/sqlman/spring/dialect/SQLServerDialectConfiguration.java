package io.sqlman.spring.dialect;

import io.sqlman.SqlDialectSupport;
import io.sqlman.support.SQLServerDialectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * SQLServer方言自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:38
 */
@Configuration
@EnableConfigurationProperties(SQLServerDialectProperties.class)
@ConditionalOnClass(SQLServerDialectSupport.class)
@ConditionalOnProperty(prefix = "sqlman.dialect", name = "type", havingValue = "SQLServer")
public class SQLServerDialectConfiguration {

    @Resource
    private SQLServerDialectProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlDialectSupport.class)
    public SQLServerDialectSupport sqlmanSQLServerDialectSupport() {
        String table = properties.getTable();
        return new SQLServerDialectSupport(table);
    }

}
