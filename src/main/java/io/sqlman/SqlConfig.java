package io.sqlman;

/**
 * 框架配置接口
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 9:48
 */
public interface SqlConfig {

    /**
     * 升级记录表表名
     *
     * @return 升级记录表表名
     */
    String getName();

    /**
     * 连接的事务隔离界别，为{@code null}时采用连接的默认隔离级别
     *
     * @return 连接的事务隔离界别
     */
    SqlIsolation getIsolation();

    /**
     * 脚本文件ANT路径表达式
     *
     * @return 脚本文件ANT路径表达式
     */
    String getLocation();

    /**
     * 脚本字符集
     *
     * @return 脚本字符集
     */
    String getCharset();

}
