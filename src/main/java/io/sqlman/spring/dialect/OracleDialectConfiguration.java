package io.sqlman.spring.dialect;

import io.sqlman.support.OracleDialectSupport;
import io.sqlman.support.SqlDialectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Oracle方言自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:38
 */
@Configuration
@EnableConfigurationProperties(OracleDialectProperties.class)
@ConditionalOnClass(OracleDialectSupport.class)
@ConditionalOnProperty(prefix = "sqlman.dialect", name = "type", havingValue = "Oracle")
public class OracleDialectConfiguration {

    @Resource
    private OracleDialectProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlDialectSupport.class)
    public OracleDialectSupport sqlmanOracleDialectSupport() {
        String table = properties.getTable();
        return new OracleDialectSupport(table);
    }

}
