package io.sqlman.spring.dialect;

/**
 * 抽象的方言配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:28
 */
public abstract class AbstractDialectProperties {
    /**
     * dataSource dialect. MySQL, SQLite, Oracle, SQLServer, eg.
     */
    private String type = "MySQL";
    /**
     * sqlman version table name
     */
    private String table = "SQLMAN";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
