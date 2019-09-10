package io.sqlman.core.spring.script;

/**
 * 抽象的脚本资源命名策略配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:03
 */
public abstract class AbstractNamingProperties {
    /**
     * SQL script naming strategy implementation
     */
    private String strategy = "standard";

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
