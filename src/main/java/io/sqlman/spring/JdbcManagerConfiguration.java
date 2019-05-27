package io.sqlman.spring;

import io.sqlman.*;
import io.sqlman.manager.JdbcIsolation;
import io.sqlman.manager.JdbcVersionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
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

    @Resource
    private SqlLoggerSupplier loggerSupplier;

    @Bean
    @ConditionalOnMissingBean(SqlVersionManager.class)
    @ConditionalOnProperty(prefix = "sqlman", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JdbcVersionManager sqlmanBasicVersionManager(ApplicationContext applicationContext) throws SQLException {
        Map<String, DataSource> map = applicationContext.getBeansOfType(DataSource.class);
        if (map.isEmpty()) {
            throw new IllegalStateException("no dataSource found in application context");
        }
        DataSource dataSource = map.size() == 1 ? map.values().iterator().next() : map.get(properties.getDataSource());
        if (dataSource == null) {
            throw new IllegalStateException("no dataSource found in application context named: " + properties.getDataSource());
        }
        JdbcIsolation jdbcIsolation = properties.getJdbcIsolation();
        JdbcVersionManager jdbcVersionManager = new JdbcVersionManager(
                dataSource,
                jdbcIsolation,
                scriptProvider,
                scriptResolver,
                dialectSupport,
                loggerSupplier
        );
        jdbcVersionManager.upgrade();
        return jdbcVersionManager;
    }

}
