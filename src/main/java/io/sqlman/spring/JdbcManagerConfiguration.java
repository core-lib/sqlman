package io.sqlman.spring;

import io.sqlman.manager.JdbcIsolation;
import io.sqlman.manager.JdbcVersionManager;
import io.sqlman.provider.SqlSourceProvider;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.support.SqlDialectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 基础SQL脚本版本管理器自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:43
 */
@Configuration
@EnableConfigurationProperties(JdbcManagerProperties.class)
@ConditionalOnClass(JdbcVersionManager.class)
@ConditionalOnProperty(prefix = "sqlman", name = "manager", havingValue = "jdbc", matchIfMissing = true)
public class JdbcManagerConfiguration {

    @Resource
    private JdbcManagerProperties properties;

    @Resource
    private SqlSourceProvider scriptProvider;

    @Resource
    private SqlScriptResolver scriptResolver;

    @Resource
    private SqlDialectSupport dialectSupport;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "sqlman", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JdbcVersionManager sqlmanBasicVersionManager(ApplicationContext applicationContext) throws Exception {
        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
        if (dataSources.isEmpty()) {
            throw new IllegalStateException("no dataSource found in application context");
        }
        DataSource dataSource = dataSources.size() == 1 ? dataSources.values().iterator().next() : dataSources.get(properties.getDataSource());
        if (dataSource == null) {
            throw new IllegalStateException("no dataSource found in application context named: " + properties.getDataSource());
        }
        JdbcIsolation jdbcIsolation = properties.getJdbcIsolation();
        JdbcVersionManager jdbcVersionManager = new JdbcVersionManager(dataSource, jdbcIsolation, scriptProvider, scriptResolver, dialectSupport);
        jdbcVersionManager.upgrade();
        return jdbcVersionManager;
    }

}
