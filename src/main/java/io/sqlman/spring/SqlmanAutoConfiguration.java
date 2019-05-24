package io.sqlman.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Sqlman自动配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 16:43
 */
@Configuration
@EnableConfigurationProperties(SqlmanConfigProperties.class)
@ConditionalOnClass(SqlmanVersionManager.class)
@ConditionalOnProperty(prefix = "sqlman", value = "enabled", matchIfMissing = true)
public class SqlmanAutoConfiguration {

    @Resource
    private SqlmanConfigProperties sqlmanConfigProperties;

    @Resource
    private DataSource dataSource;

    @Bean
    @ConditionalOnMissingBean(SqlmanVersionManager.class)
    public SqlmanVersionManager sqlmanSpringUpgrader() {
        SqlmanVersionManager manager = new SqlmanVersionManager();
        return manager;
    }

}
