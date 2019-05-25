package io.sqlman.spring.script;

import io.sqlman.provider.BasicScriptProvider;
import io.sqlman.provider.SqlNamingStrategy;
import io.sqlman.provider.SqlScriptProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 基础脚本资源提供器自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:59
 */
@Configuration
@EnableConfigurationProperties(ClasspathProviderProperties.class)
@ConditionalOnClass(BasicScriptProvider.class)
@ConditionalOnProperty(prefix = "sqlman.script", name = "provider", havingValue = "classpath", matchIfMissing = true)
public class ClasspathProviderConfiguration {

    @Resource
    private ClasspathProviderProperties properties;

    @Resource
    private SqlNamingStrategy sqlNamingStrategy;

    @Bean
    @ConditionalOnMissingBean(SqlScriptProvider.class)
    public BasicScriptProvider sqlmanBasicScriptProvider() {
        String location = properties.getLocation();
        return new BasicScriptProvider(location, sqlNamingStrategy);
    }

}