package io.sqlman.core.script;

import io.sqlman.core.SqlSentence;

/**
 * 基于druid的SQL脚本语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:15
 */
public class DruidSentence implements SqlSentence {
    private final int ordinal;
    private final String value;
    private final String schema;
    private final String table;

    public DruidSentence(int ordinal, String value, String schema, String table) {
        if (ordinal < 1) {
            throw new IllegalArgumentException("ordinal must not lesser than 1");
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("value must not be null or blank string");
        }
        this.ordinal = ordinal;
        this.value = value;
        this.schema = schema;
        this.table = table;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public String table() {
        return table;
    }

    @Override
    public String toString() {
        return value;
    }
}
