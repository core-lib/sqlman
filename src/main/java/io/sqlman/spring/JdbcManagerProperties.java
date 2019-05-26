package io.sqlman.spring;

import io.sqlman.manager.JdbcIsolation;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础SQL脚本版本管理器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:40
 */
@ConfigurationProperties(prefix = "sqlman")
public class JdbcManagerProperties extends AbstractManagerProperties {
    private JdbcIsolation jdbcIsolation = JdbcIsolation.DEFAULT;

    public JdbcIsolation getJdbcIsolation() {
        return jdbcIsolation;
    }

    public void setJdbcIsolation(JdbcIsolation jdbcIsolation) {
        this.jdbcIsolation = jdbcIsolation;
    }
}
