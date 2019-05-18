package io.sqlman;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * SQL脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:00
 */
public interface SqlScript {

    /**
     * 执行脚本
     *
     * @param connection 数据库连接
     * @param index      执行语句下标
     * @throws SQLException SQL执行异常
     */
    void execute(Connection connection, int index) throws SQLException;

    /**
     * SQL语句数量
     *
     * @return 语句数量
     */
    int sqls();

    /**
     * SQL语句列表
     *
     * @return 语句列表
     */
    Enumeration<SqlStatement> statements();

    /**
     * 脚本版本号
     *
     * @return 脚本版本号
     */
    String version();

    /**
     * 脚本描述
     *
     * @return 脚本描述
     */
    String description();

    /**
     * 脚本作者
     *
     * @return 脚本作者
     */
    String author();

}
