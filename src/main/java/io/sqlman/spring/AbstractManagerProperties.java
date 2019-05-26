package io.sqlman.spring;

/**
 * 抽象SQL脚本版本管理器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:39
 */
public class AbstractManagerProperties {
    /**
     * whether the sqlman should be enabled
     */
    private boolean enabled = true;
    /**
     * sqlman implementation name
     */
    private String manager = "jdbc";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
