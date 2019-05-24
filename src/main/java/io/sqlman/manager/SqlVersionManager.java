package io.sqlman.manager;

/**
 * SQL版本管理器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 12:27
 */
public interface SqlVersionManager {

    /**
     * 执行SQL脚本升级
     *
     * @throws Exception 执行异常
     */
    void upgrade() throws Exception;

}
