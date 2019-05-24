package io.sqlman;

import java.util.Enumeration;

/**
 * SQL脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:00
 */
public interface SqlScript {

    /**
     * SQL语句数量
     *
     * @return 语句数量
     */
    int sqls();

    /**
     * 获取该脚本指定序号的SQL语句
     *
     * @param ordinal SQL语句序号
     * @return 指定序号的SQL语句
     * @throws IndexOutOfBoundsException 序号超出边界时抛出
     */
    SqlStatement statement(int ordinal) throws IndexOutOfBoundsException;

    /**
     * SQL语句列表
     *
     * @return 语句列表
     */
    Enumeration<SqlStatement> statements();

    /**
     * 脚本版本号
     *
     * @return 脚本版本号
     */
    String version();

    /**
     * 脚本描述
     *
     * @return 脚本描述
     */
    String description();

}
