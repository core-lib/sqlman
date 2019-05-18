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
     * 检查数据库状态，当返回为{@code null}时表示版本升级记录表不存在。
     * 否则表示版本升级记录表已存在，且当前版本为{@link SqlStatus#getVersion()}下标为{@link SqlStatus#getIndex()}
     *
     * @param connection 连接
     * @param config     系统配置
     * @return 数据库状态
     * @throws SQLException SQL异常
     */
    SqlStatus check(Connection connection, SqlConfig config) throws SQLException;

    /**
     * 初始化版本升级记录表
     *
     * @param connection 连接
     * @param config     系统配置
     * @throws SQLException SQL异常
     */
    void initialize(Connection connection, SqlConfig config) throws SQLException;

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
