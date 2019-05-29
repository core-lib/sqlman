package io.sqlman.spring.script;

import io.sqlman.SqlScriptResolver;
import io.sqlman.script.DruidScriptResolver;
import io.sqlman.source.ClasspathSourceProvider;
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
@EnableConfigurationProperties(DruidResolverProperties.class)
@ConditionalOnClass(ClasspathSourceProvider.class)
@ConditionalOnProperty(prefix = "sqlman.script", name = "resolver", havingValue = "druid", matchIfMissing = true)
public class DruidResolverConfiguration {

    @Resource
    private DruidResolverProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlScriptResolver.class)
    public DruidScriptResolver sqlmanBasicScriptResolver() {
        String dialect = properties.getDialect();
        String charset = properties.getCharset();
        return new DruidScriptResolver(dialect, charset);
    }

}
