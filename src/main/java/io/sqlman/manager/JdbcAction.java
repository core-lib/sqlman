package io.sqlman.manager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库操作
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 23:54
 */
public interface JdbcAction {

    /**
     * 执行操作
     *
     * @param connection 连接
     * @throws SQLException 操作异常
     * @throws Exception    操作异常
     */
    void perform(Connection connection) throws Exception;

}
