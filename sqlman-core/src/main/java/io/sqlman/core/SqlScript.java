package io.sqlman.core;

import java.util.Enumeration;
import java.util.Set;

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
     * 序号从{@code 1}开始
     *
     * @param ordinal SQL语句序号
     * @return 指定序号的SQL语句
     * @throws IndexOutOfBoundsException 序号超出边界时抛出
     */
    SqlSentence sentence(int ordinal) throws IndexOutOfBoundsException;

    /**
     * SQL语句列表
     *
     * @return 语句列表
     */
    Enumeration<SqlSentence> sentences();

    /**
     * 脚本名称
     *
     * @return 脚本名称
     */
    String name();

    /**
     * 脚本版本号
     *
     * @return 脚本版本号
     */
    String version();

    /**
     * 指令列表
     *
     * @return 指令列表
     */
    Set<String> instructions();

    /**
     * 脚本描述
     *
     * @return 脚本描述
     */
    String description();

}
