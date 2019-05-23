package io.sqlman;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQL方言
 * 根据系统的数据库类型而选，由于不同数据库的 DQL, DML, DDL, DCL 差异比较大，所以通过不同的实现类来支持。
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:13
 */
public interface SqlDialect {

    /**
     * 方言类型
     *
     * @return 方言类型
     */
    String type();

    /**
     * 安装版本升级记录表，如果表已经安装则不做任何变化。
     *
     * @param connection 连接
     * @param config     系统配置
     * @throws SQLException SQL异常
     */
    void install(Connection connection, SqlConfig config) throws SQLException;

    /**
     * 查询数据库的最新版本升级记录，当返回为{@code null}时表示版本升级记录表没有任何记录。
     *
     * @param connection 连接
     * @param config     系统配置
     * @return 数据库状态
     * @throws SQLException SQL异常
     */
    SqlVersion status(Connection connection, SqlConfig config) throws SQLException;

    /**
     * 升级到指定状态
     *
     * @param connection 连接
     * @param config     系统配置
     * @param version    版本
     * @return 影响行数
     * @throws SQLException SQL异常
     */
    int upgrade(Connection connection, SqlConfig config, SqlVersion version) throws SQLException;

}
