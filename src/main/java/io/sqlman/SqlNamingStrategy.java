package io.sqlman;

import io.sqlman.exception.MalformedNameException;

import java.util.Comparator;

/**
 * SQL脚本命名策略
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 21:57
 */
public interface SqlNamingStrategy extends Comparator<String> {

    /**
     * 验证命名是否合法
     *
     * @param name SQL脚本名称
     * @return 如果名称合法则返回{@code true}否则返回{@code false}
     */
    boolean check(String name);

    /**
     * SQL脚本名称解析
     *
     * @param name SQL脚本名称
     * @return SQL脚本信息
     * @throws MalformedNameException SQL脚本资源命名不合法，即{@link this#check(String)}返回{@code false}。
     */
    SqlNaming parse(String name) throws MalformedNameException;

}
