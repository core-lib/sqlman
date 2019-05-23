package io.sqlman;

/**
 * 框架配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:25
 */
public class SimpleConfig implements SqlConfig {
    private String tableName = "sqlman";

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
