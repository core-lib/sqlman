package io.sqlman.statement;

import io.sqlman.SqlStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 可执行的SQL语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:14
 */
public abstract class ExecutableStatement implements SqlStatement {

    @Override
    public void execute(Connection connection) throws SQLException {

    }
}
