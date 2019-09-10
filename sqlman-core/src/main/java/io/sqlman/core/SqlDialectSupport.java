package io.sqlman.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQL方言
 * 根据系统的数据库类型而选，由于不同数据库的 DQL, DML, DDL, DCL 差异比较大，所以通过不同的实现类来支持。
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:13
 */
public interface SqlDialectSupport {

    /**
     * 创建版本升级记录表，如果已经创建则不做任何变化。
     *
     * @param connection 连接
     * @throws SQLException SQL异常
     */
    void create(Connection connection) throws SQLException;

    /**
     * 检测当前版本，如果没有任何升级记录则返回{@code null}
     *
     * @param connection 连接
     * @return 当前版本
     * @throws SQLException SQL异常
     */
    SqlVersion detect(Connection connection) throws SQLException;

    /**
     * 更新当前版本
     *
     * @param connection 连接
     * @param version    当前版本
     * @throws SQLException SQL异常
     */
    void update(Connection connection, SqlVersion version) throws SQLException;

    /**
     * 删除版本升级记录表，如果已经删除则不做任何变化。
     *
     * @param connection 连接
     * @throws SQLException SQL异常
     */
    void remove(Connection connection) throws SQLException;

    /**
     * 获取版本升级的排他锁，当获取失败时抛出{@link SQLException}
     *
     * @param connection 连接
     * @throws SQLException SQL异常
     */
    void lockup(Connection connection) throws SQLException;

    /**
     * 释放版本升级的排他锁
     *
     * @param connection 连接
     * @throws SQLException SQL异常
     */
    void unlock(Connection connection) throws SQLException;

    /**
     * 备份表
     *
     * @param connection 连接
     * @param script     脚本
     * @param ordinal    语句下标
     * @throws SQLException SQL异常
     */
    void backup(Connection connection, SqlScript script, int ordinal) throws SQLException;

}
