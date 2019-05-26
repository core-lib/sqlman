package io.sqlman.manager;

import java.sql.Connection;

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
     * @throws Exception 事务执行异常
     */
    T execute(Connection connection) throws Exception;

}