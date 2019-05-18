package io.sqlman;

/**
 * SQL类型
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:06
 */
public enum SqlType {

    /**
     * 数据查询语言
     */
    DQL(false),
    /**
     * 数据维护语言
     */
    DML(true),
    /**
     * 数据定义语言
     */
    DDL(false),
    /**
     * 数据控制语言
     */
    DCL(false);

    /**
     * 可回滚的
     */
    public final boolean cancelable;

    SqlType(boolean cancelable) {
        this.cancelable = cancelable;
    }
}
