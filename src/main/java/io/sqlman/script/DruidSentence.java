package io.sqlman.script;

import io.sqlman.SqlSentence;

/**
 * 基于druid的SQL脚本语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:15
 */
public class DruidSentence implements SqlSentence {
    private final int ordinal;
    private final String value;

    public DruidSentence(int ordinal, String value) {
        if (ordinal < 1) {
            throw new IllegalArgumentException("ordinal must not lesser than 1");
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("value must not be null or blank string");
        }
        this.ordinal = ordinal;
        this.value = value;
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
    public String toString() {
        return value;
    }
}
