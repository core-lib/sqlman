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
     * 获取所有SQL脚本，相当于调用{@link this.acquire(String)}以{@code null}作为参数。
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @param config 系统配置
     * @return 所有SQL脚本
     * @throws Exception 脚本获取错误
     */
    Enumeration<SqlResource> acquire(SqlConfig config) throws Exception;

}
