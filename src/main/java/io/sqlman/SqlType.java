package io.sqlman;

import com.alibaba.druid.util.JdbcUtils;

/**
 * @author Payne 646742615@qq.com
 * 2019/5/24 9:44
 */
public enum SqlType {

    /**
     * MySQL
     */
    MySQL(JdbcUtils.MYSQL),

    /**
     * Oracle
     */
    Oracle(JdbcUtils.ORACLE),

    /**
     * SQLServer
     */
    SQLServer(JdbcUtils.SQL_SERVER),

    /**
     * SQLite
     */
    SQLite(JdbcUtils.SQLITE);

    public final String value;

    SqlType(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        this.value = value;
    }

}
