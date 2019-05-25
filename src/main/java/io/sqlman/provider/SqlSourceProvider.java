package io.sqlman.provider;

import io.sqlman.SqlSource;

import java.io.IOException;
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
     * 实现类当发现脚本资源中包含重复的SQL脚本版本时应当抛出
     *
     * @return 所有SQL脚本
     * @throws MalformedNameException     SQL脚本资源命名不合法
     * @throws DuplicatedVersionException SQL脚本资源版本重复
     * @throws IOException                I/O异常
     */
    Enumeration<SqlSource> acquire() throws MalformedNameException, DuplicatedVersionException, IOException;

    /**
     * 获取从指定版本起始的所有SQL脚本，当included参数为{@code true}时包括起始版本，否则不包含起始版本。
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @param version  起始版本
     * @param included 是否包含起始版本
     * @return 所有SQL脚本
     * @throws MalformedNameException     SQL脚本资源命名不合法
     * @throws DuplicatedVersionException SQL脚本资源版本重复
     * @throws IOException                I/O异常
     */
    Enumeration<SqlSource> acquire(String version, boolean included) throws MalformedNameException, DuplicatedVersionException, IOException;

}
