package io.sqlman.spring;

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

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
