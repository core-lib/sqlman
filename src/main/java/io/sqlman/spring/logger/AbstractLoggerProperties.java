package io.sqlman.spring.logger;

import io.sqlman.SqlLogger;

/**
 * 抽象的脚本资源命名策略配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:03
 */
public abstract class AbstractLoggerProperties {
    /**
     * SQL logger supplier name
     */
    private String supplier = "slf4j";

    /**
     * SQL logger level. TRACE, DEBUG, INFO, WARN, ERROR.
     */
    private SqlLogger.Level level = SqlLogger.Level.INFO;

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public SqlLogger.Level getLevel() {
        return level;
    }

    public void setLevel(SqlLogger.Level level) {
        this.level = level;
    }
}
