package io.sqlman;

/**
 * SQL语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:19
 */
public interface SqlStatement {

    /**
     * 脚本序号
     *
     * @return 脚本序号
     */
    int ordinal();

    /**
     * 脚本语句
     *
     * @return 脚本语句
     */
    String statement();

}
