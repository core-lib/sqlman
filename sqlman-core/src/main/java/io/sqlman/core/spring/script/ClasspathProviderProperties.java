package io.sqlman.core.spring.script;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础脚本资源提供器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:52
 */
@ConfigurationProperties(prefix = "sqlman.script")
public class ClasspathProviderProperties extends AbstractProviderProperties {
    /**
     * SQL script location ANT path pattern
     */
    private String location = "sqlman/**/*.sql";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
