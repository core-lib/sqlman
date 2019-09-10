package io.sqlman.core.spring.logger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础脚本资源命名策略配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:54
 */
@ConfigurationProperties(prefix = "sqlman.logger")
public class Slf4jLoggerProperties extends AbstractLoggerProperties {

}
