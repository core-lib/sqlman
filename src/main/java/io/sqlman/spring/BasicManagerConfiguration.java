package io.sqlman.spring;

import io.sqlman.manager.JdbcIsolation;
import io.sqlman.manager.JdbcVersionManager;
import io.sqlman.provider.SqlSourceProvider;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.support.SqlDialectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 基础SQL脚本版本管理器自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:43
 */
@Configuration
@EnableConfigurationProperties(BasicManagerProperties.class)
@ConditionalOnClass(JdbcVersionManager.class)
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(prefix = "sqlman", name = "manager", havingValue = "basic", matchIfMissing = true)
public class BasicManagerConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private BasicManagerProperties properties;

    @Resource
    private SqlSourceProvider scriptProvider;

    @Resource
    private SqlScriptResolver scriptResolver;

    @Resource
    private SqlDialectSupport dialectSupport;

    @Bean
    @ConditionalOnMissingBean
    public JdbcVersionManager sqlmanBasicVersionManager() throws Exception {
        JdbcIsolation trxIsolation = properties.getTrxIsolation();
        JdbcVersionManager jdbcVersionManager = new JdbcVersionManager(dataSource, trxIsolation, scriptProvider, scriptResolver, dialectSupport);
        jdbcVersionManager.upgrade();
        return jdbcVersionManager;
    }

}
