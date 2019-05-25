package io.sqlman.spring.dialect;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SQLite方言配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:29
 */
@ConfigurationProperties("sqlman.dialect")
public class SQLiteDialectProperties extends AbstractDialectProperties {
}
