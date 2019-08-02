package io.sqlman.spring;

import io.sqlman.version.JdbcIsolation;
import io.sqlman.version.JdbcMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础SQL脚本版本管理器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:40
 */
@ConfigurationProperties(prefix = "sqlman")
public class JdbcManagerProperties extends AbstractManagerProperties {
    /**
     * the dataSource bean name. if your application has more than one dataSources
     */
    private String dataSource = "dataSource";

    /**
     * default transaction isolation level
     */
    private JdbcIsolation defaultIsolation;

    /**
     * default mode: SAFETY or DANGER which means backup or not backup before the sql sentence being execute
     */
    private JdbcMode defaultMode;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcIsolation getDefaultIsolation() {
        return defaultIsolation;
    }

    public void setDefaultIsolation(JdbcIsolation defaultIsolation) {
        this.defaultIsolation = defaultIsolation;
    }

    public JdbcMode getDefaultMode() {
        return defaultMode;
    }

    public void setDefaultMode(JdbcMode defaultMode) {
        this.defaultMode = defaultMode;
    }
}
