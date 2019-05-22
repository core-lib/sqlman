package io.sqlman;

/**
 * SQL脚本执行器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 12:27
 */
public interface SqlExecutor {

    /**
     * 执行SQL脚本升级
     *
     * @throws Exception 执行异常
     */
    void execute() throws Exception;

}
