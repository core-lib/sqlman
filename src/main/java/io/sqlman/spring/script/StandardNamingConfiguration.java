package io.sqlman.spring.script;

import io.sqlman.strategy.SqlNamingStrategy;
import io.sqlman.strategy.StandardNamingStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 基础脚本资源命名策略自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:55
 */
@Configuration
@EnableConfigurationProperties(StandardNamingProperties.class)
@ConditionalOnClass(StandardNamingStrategy.class)
@ConditionalOnProperty(prefix = "sqlman.script.naming", name = "strategy", havingValue = "standard", matchIfMissing = true)
public class StandardNamingConfiguration {

    @Resource
    private StandardNamingProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlNamingStrategy.class)
    public SqlNamingStrategy sqlmanBasicNamingStrategy() {
        char separator = properties.getSeparator();
        String delimiter = properties.getDelimiter();
        String extension = properties.getExtension();
        return new StandardNamingStrategy(separator, delimiter, extension);
    }

}
