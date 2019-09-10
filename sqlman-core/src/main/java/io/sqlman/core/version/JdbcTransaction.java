package io.sqlman.core.version;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库事务
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 10:57
 */
public interface JdbcTransaction<T> {

    /**
     * 执行事务
     *
     * @param connection 连接
     * @return 事务执行结果
     * @throws SQLException SQL异常
     */
    T execute(Connection connection) throws SQLException;

}