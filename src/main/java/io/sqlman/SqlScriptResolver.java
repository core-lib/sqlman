package io.sqlman;

import io.sqlman.exception.IncorrectSyntaxException;

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
     * @param source 脚本资源
     * @return SQL脚本
     * @throws IncorrectSyntaxException 语法错误异常
     * @throws IOException              I/O异常
     */
    SqlScript resolve(SqlSource source) throws IncorrectSyntaxException, IOException;

}
