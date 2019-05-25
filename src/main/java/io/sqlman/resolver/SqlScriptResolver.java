package io.sqlman.resolver;

import io.sqlman.SqlScript;
import io.sqlman.SqlSource;

/**
 * SQL脚本解析器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:38
 */
public interface SqlScriptResolver {

    /**
     * 解析SQL脚本
     *
     * @param resource 脚本资源
     * @return SQL脚本
     * @throws Exception 解析异常
     */
    SqlScript resolve(SqlSource resource) throws Exception;

}
