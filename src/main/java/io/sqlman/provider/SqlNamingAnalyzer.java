package io.sqlman.provider;

import java.util.Comparator;

/**
 * SQL脚本名称解析器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 20:55
 */
interface SqlNamingAnalyzer extends Comparator<String> {

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
    SqlInfo parse(String name) throws MalformedNameException;

}
