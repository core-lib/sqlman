package io.sqlman.provider;

import io.sqlman.SqlSource;

import java.util.Enumeration;

/**
 * SQL脚本提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 17:59
 */
public interface SqlSourceProvider extends SqlNamingAnalyzer {

    /**
     * 获取所有SQL脚本
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @return 所有SQL脚本
     * @throws Exception 脚本获取错误
     */
    Enumeration<SqlSource> acquire() throws Exception;

    /**
     * 获取从指定版本起始的所有SQL脚本，当included参数为{@code true}时包括起始版本，否则不包含起始版本。
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @param version  起始版本
     * @param included 是否包含起始版本
     * @return 所有SQL脚本
     * @throws Exception 脚本获取错误
     */
    Enumeration<SqlSource> acquire(String version, boolean included) throws Exception;

}
