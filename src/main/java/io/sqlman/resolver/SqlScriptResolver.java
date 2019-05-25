package io.sqlman.resolver;

import io.sqlman.SqlScript;
import io.sqlman.SqlSource;

import java.io.IOException;

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
     * @throws IncorrectSyntaxException 语法错误异常
     * @throws IOException              I/O异常
     */
    SqlScript resolve(SqlSource resource) throws IncorrectSyntaxException, IOException;

}
