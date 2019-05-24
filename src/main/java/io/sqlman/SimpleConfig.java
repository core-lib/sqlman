package io.sqlman;

/**
 * 框架配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:25
 */
public class SimpleConfig implements SqlConfig {
    private String name = "sqlman";
    private SqlIsolation isolation = SqlIsolation.DEFAULT;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SqlIsolation getIsolation() {
        return isolation;
    }

    public void setIsolation(SqlIsolation isolation) {
        this.isolation = isolation;
    }
}
