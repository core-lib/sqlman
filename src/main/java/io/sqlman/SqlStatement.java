package io.sqlman;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQL语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 11:19
 */
public interface SqlStatement {

    /**
     * 执行语句
     *
     * @param connection 数据库连接
     * @throws SQLException SQL执行异常
     */
    void execute(Connection connection) throws SQLException;

    /**
     * 脚本类型
     *
     * @return 脚本类型
     */
    SqlType type();

    /**
     * 脚本序号
     *
     * @return 脚本序号
     */
    int ordinal();

    /**
     * 脚本语句
     *
     * @return 脚本语句
     */
    String statement();

}
