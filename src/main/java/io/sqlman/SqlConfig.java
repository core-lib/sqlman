package io.sqlman;

/**
 * 框架配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:25
 */
public class SqlConfig {
    private String name = "sqlman";

    public SqlConfig() {
    }

    public SqlConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
