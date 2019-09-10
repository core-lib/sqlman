package io.sqlman.core.spring.logger;

import io.sqlman.core.SqlLoggerSupplier;
import io.sqlman.core.logger.Slf4jLoggerSupplier;
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
@EnableConfigurationProperties(Slf4jLoggerProperties.class)
@ConditionalOnClass(Slf4jLoggerSupplier.class)
@ConditionalOnProperty(prefix = "sqlman.logger", name = "supplier", havingValue = "slf4j", matchIfMissing = true)
public class Slf4jLoggerConfiguration {

    @Resource
    private Slf4jLoggerProperties properties;

    @Bean
    @ConditionalOnMissingBean(SqlLoggerSupplier.class)
    public SqlLoggerSupplier sqlmanSlf4jLoggerSupplier() {
        return new Slf4jLoggerSupplier(properties.getLevel());
    }

}
