package io.sqlman.git.spring;

import io.sqlman.core.SqlNamingStrategy;
import io.sqlman.core.SqlSourceProvider;
import io.sqlman.git.GitClientFactory;
import io.sqlman.git.GitConfig;
import io.sqlman.git.source.GitSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Git资源提供器配置
 *
 * @author Payne 646742615@qq.com
 * 2019/9/11 16:17
 */
@Configuration
@EnableConfigurationProperties(GitProviderProperties.class)
@ConditionalOnClass(GitSourceProvider.class)
@ConditionalOnProperty(prefix = "sqlman.script", name = "provider", havingValue = "git")
public class GitProviderConfiguration {

    @Resource
    private GitProviderProperties properties;

    @Resource
    private SqlNamingStrategy sqlNamingStrategy;

    @Bean
    @ConditionalOnMissingBean(SqlSourceProvider.class)
    public GitSourceProvider sqlmanGitScriptProvider() {
        GitConfig config = new GitConfig(
                properties.getClone(),
                properties.getCheckout(),
                properties.getClean(),
                properties.getPull()
        );
        GitClientFactory clientFactory = new GitClientFactory(config);
        GitSourceProvider sourceProvider = new GitSourceProvider(sqlNamingStrategy, clientFactory);
        if (properties.getDirectory() != null) {
            sourceProvider.setDirectory(properties.getDirectory());
        }
        if (properties.getBranch() != null) {
            sourceProvider.setBranch(properties.getBranch());
        }
        if (properties.getUpdateStrategy() != null) {
            sourceProvider.setUpdateStrategy(properties.getUpdateStrategy());
        }
        if (properties.getLocation() != null) {
            sourceProvider.setScriptLocation(properties.getLocation());
        }
        return sourceProvider;
    }

}
