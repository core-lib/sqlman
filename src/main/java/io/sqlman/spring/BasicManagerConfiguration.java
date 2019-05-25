package io.sqlman.spring;

import io.sqlman.manager.BasicVersionManager;
import io.sqlman.manager.SqlIsolation;
import io.sqlman.provider.SqlScriptProvider;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.support.SqlDialectSupport;
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
@ConditionalOnClass(BasicVersionManager.class)
@ConditionalOnProperty(prefix = "sqlman", name = "manager", havingValue = "basic", matchIfMissing = true)
public class BasicManagerConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private BasicManagerProperties properties;

    @Resource
    private SqlScriptProvider scriptProvider;

    @Resource
    private SqlScriptResolver scriptResolver;

    @Resource
    private SqlDialectSupport dialectSupport;

    @Bean
    @ConditionalOnMissingBean
    public BasicVersionManager sqlmanBasicVersionManager() throws Exception {
        SqlIsolation trxIsolation = properties.getTrxIsolation();
        BasicVersionManager basicVersionManager = new BasicVersionManager(dataSource, trxIsolation, scriptProvider, scriptResolver, dialectSupport);
        basicVersionManager.upgrade();
        return basicVersionManager;
    }

}