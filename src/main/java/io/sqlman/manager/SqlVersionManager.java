package io.sqlman.manager;

import io.sqlman.SqlScript;
import io.sqlman.SqlSource;
import io.sqlman.SqlVersion;
import io.sqlman.provider.DuplicatedVersionException;
import io.sqlman.provider.MalformedNameException;
import io.sqlman.resolver.IncorrectSyntaxException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

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
     * @throws SQLException SQL异常
     */
    void upgrade() throws SQLException;

    /**
     * 获取所有SQL脚本
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     * 实现类当发现脚本资源中包含重复的SQL脚本版本时应当抛出
     *
     * @return 所有SQL脚本
     * @throws MalformedNameException     SQL脚本资源命名不合法
     * @throws DuplicatedVersionException SQL脚本资源版本重复
     * @throws IOException                I/O异常
     */
    Enumeration<SqlSource> acquire() throws MalformedNameException, DuplicatedVersionException, IOException;

    /**
     * 获取从指定版本起始的所有SQL脚本，当included参数为{@code true}时包括起始版本，否则不包含起始版本。
     * 实现类返回的结果必须遵循脚本版本的先后顺序。
     *
     * @param version  起始版本
     * @param included 是否包含起始版本
     * @return 所有SQL脚本
     * @throws MalformedNameException     SQL脚本资源命名不合法
     * @throws DuplicatedVersionException SQL脚本资源版本重复
     * @throws IOException                I/O异常
     */
    Enumeration<SqlSource> acquire(String version, boolean included) throws MalformedNameException, DuplicatedVersionException, IOException;

    /**
     * 解析SQL脚本
     *
     * @param resource 脚本资源
     * @return SQL脚本
     * @throws IncorrectSyntaxException 语法错误异常
     * @throws IOException              I/O异常
     */
    SqlScript resolve(SqlSource resource) throws IncorrectSyntaxException, IOException;

    /**
     * 创建版本升级记录表，如果已经创建则不做任何变化。
     *
     * @throws SQLException SQL异常
     */
    void create() throws SQLException;

    /**
     * 检测当前版本，如果没有任何升级记录则返回{@code null}
     *
     * @return 当前版本
     * @throws SQLException SQL异常
     */
    SqlVersion detect() throws SQLException;

    /**
     * 更新当前版本
     *
     * @param version 当前版本
     * @throws SQLException SQL异常
     */
    void update(SqlVersion version) throws SQLException;

    /**
     * 删除版本升级记录表，如果已经删除则不做任何变化。
     *
     * @throws SQLException SQL异常
     */
    void remove() throws SQLException;

    /**
     * 获取版本升级的排他锁，当获取失败时抛出{@link SQLException}
     *
     * @throws SQLException SQL异常
     */
    void lockup() throws SQLException;

    /**
     * 释放版本升级的排他锁
     *
     * @throws SQLException SQL异常
     */
    void unlock() throws SQLException;

}
