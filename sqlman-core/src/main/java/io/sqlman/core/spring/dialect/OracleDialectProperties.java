package io.sqlman.core.spring.dialect;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Oracle方言配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:29
 */
@ConfigurationProperties("sqlman.dialect")
public class OracleDialectProperties extends AbstractDialectProperties {
}
