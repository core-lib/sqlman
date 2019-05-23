package io.sqlman;

import java.util.Enumeration;

/**
 * SQL脚本提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 17:59
 */
public interface SqlProvider {

    /**
     * 获取所有SQL脚本，相当于调用{@link SqlProvider#acquire(String)}以{@code null}作为参数。
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @param dbType 数据库类型
     * @return 所有SQL脚本
     * @throws Exception 脚本获取错误
     */
    Enumeration<SqlScript> acquire(String dbType) throws Exception;

    /**
     * 获取包括起始版本及更新版本的SQL脚本，当 version 为{@code null} 时及表示获取所有脚本。
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @param dbType 数据库类型
     * @param version 起始版本
     * @return 包括起始版本及更新版本的SQL脚本
     * @throws Exception 脚本获取错误
     */
    Enumeration<SqlScript> acquire(String dbType, String version) throws Exception;

}
