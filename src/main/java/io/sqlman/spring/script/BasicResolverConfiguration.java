package io.sqlman.spring.script;

import io.sqlman.provider.BasicScriptProvider;
import io.sqlman.resolver.BasicScriptResolver;
import io.sqlman.resolver.SqlScriptResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * druid脚本资源解析器自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:30
 */
@Configuration
@EnableConfigurationProperties(BasicResolverProperties.class)
@ConditionalOnClass(BasicScriptProvider.class)
@ConditionalOnProperty(prefix = "sqlman.script", name = "resolver", havingValue = "basic", matchIfMissing = true)
public class BasicResolverConfiguration {

    @Resource
    private BasicResolverProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlScriptResolver.class)
    public BasicScriptResolver sqlmanBasicScriptResolver() {
        String dialect = properties.getDialect();
        String charset = properties.getCharset();
        return new BasicScriptResolver(dialect, charset);
    }

}
