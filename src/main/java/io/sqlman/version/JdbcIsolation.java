package io.sqlman.version;

import java.sql.Connection;

/**
 * 事务隔离级别
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 10:35
 */
public enum JdbcIsolation {

    /**
     * 缺省事务
     */
    DEFAULT(-1),

    /**
     * 读未提交
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

    /**
     * 读已提交
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    /**
     * 可重复读
     */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    /**
     * 序列化
     */
    SERLALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    public final int value;

    JdbcIsolation(int value) {
        this.value = value;
    }
}
