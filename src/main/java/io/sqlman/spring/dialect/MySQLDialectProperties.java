package io.sqlman.spring.dialect;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MySQL方言配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:19
 */
@ConfigurationProperties("sqlman.dialect")
public class MySQLDialectProperties extends AbstractDialectProperties {

}
