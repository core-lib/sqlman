package io.sqlman.version;

import java.sql.Connection;
import java.util.Set;

/**
 * JDBC事务隔离级别
 *
 * @author Payne 646742615@qq.com
 * 2019/6/1 10:34
 */
public enum JdbcIsolation implements JdbcInstruction {

    /**
     * 隔离级别：读未提交
     */
    READ_UNCOMMITTED(INSTRUCTION_READ_UNCOMMITTED, "read uncommitted", Connection.TRANSACTION_READ_UNCOMMITTED),
    /**
     * 隔离级别：读已提交
     */
    READ_COMMITTED(INSTRUCTION_READ_COMMITTED, "read committed", Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 隔离级别：可重复读
     */
    REPEATABLE_READ(INSTRUCTION_REPEATABLE_READ, "repeatable read", Connection.TRANSACTION_REPEATABLE_READ),
    /**
     * 隔离级别：串行执行
     */
    SERIALIZABLE(INSTRUCTION_SERIALIZABLE, "serializable", Connection.TRANSACTION_SERIALIZABLE);

    /**
     * 指令
     */
    public final String instruction;
    /**
     * 隔离级别名称
     */
    public final String name;
    /**
     * JDBC Connection 隔离级别
     */
    public final int level;

    JdbcIsolation(String instruction, String name, int level) {
        this.instruction = instruction;
        this.name = name;
        this.level = level;
    }

    /**
     * 从指令集中获取隔离级别，如果没有隔离级别指令则返回{@code null}
     *
     * @param instructions 指令集
     * @return 对应隔离级别或{@code null}当没有隔离级别指令时
     */
    public static JdbcIsolation valueOf(Set<String> instructions) {
        if (instructions == null || instructions.isEmpty()) {
            return null;
        }
        JdbcIsolation isolation = null;
        for (JdbcIsolation value : values()) {
            if (instructions.contains(value.instruction)) {
                if (isolation != null) {
                    throw new IllegalArgumentException("multiple transaction isolation level instructions");
                } else {
                    isolation = value;
                }
            }
        }
        return isolation;
    }

    @Override
    public String toString() {
        return name;
    }
}
