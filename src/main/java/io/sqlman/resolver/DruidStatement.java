package io.sqlman.resolver;

import io.sqlman.SqlStatement;

/**
 * 基本SQL脚本语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:15
 */
public class DruidStatement implements SqlStatement {
    private final int ordinal;
    private final String statement;

    public DruidStatement(int ordinal, String statement) {
        if (ordinal < 0) {
            throw new IllegalArgumentException("ordinal must not be negative");
        }
        if (statement == null || statement.trim().isEmpty()) {
            throw new IllegalArgumentException("statement must not be null or blank string");
        }
        this.ordinal = ordinal;
        this.statement = statement;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String statement() {
        return statement;
    }

    @Override
    public String toString() {
        return statement;
    }
}
