package io.sqlman;

/**
 * SQL语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:19
 */
public interface SqlStatement {

    /**
     * 语句序号
     *
     * @return 语句序号
     */
    int ordinal();

    /**
     * 语句内容
     *
     * @return 语句内容
     */
    String statement();

}
