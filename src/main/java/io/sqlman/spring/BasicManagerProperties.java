package io.sqlman.spring;

import io.sqlman.manager.SqlIsolation;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础SQL脚本版本管理器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:40
 */
@ConfigurationProperties(prefix = "sqlman")
public class BasicManagerProperties extends AbstractManagerProperties {
    private SqlIsolation trxIsolation = SqlIsolation.DEFAULT;

    public SqlIsolation getTrxIsolation() {
        return trxIsolation;
    }

    public void setTrxIsolation(SqlIsolation trxIsolation) {
        this.trxIsolation = trxIsolation;
    }
}
