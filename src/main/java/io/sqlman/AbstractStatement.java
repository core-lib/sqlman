package io.sqlman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 可执行的SQL语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:14
 */
public abstract class AbstractStatement implements SqlStatement {

    @Override
    public int execute(Connection connection) throws SQLException {
        String sql = statement();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        return statement.getUpdateCount();
    }
}
