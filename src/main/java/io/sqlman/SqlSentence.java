package io.sqlman;

/**
 * SQL语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:19
 */
public interface SqlSentence {

    /**
     * 语句序号，从{@code 1}开始
     *
     * @return 语句序号
     */
    int ordinal();

    /**
     * 语句内容
     *
     * @return 语句内容
     */
    String value();

    /**
     * 数据库名
     *
     * @return 数据库名
     */
    String schema();

    /**
     * 操作表名
     *
     * @return 操作表名
     */
    String table();
}
