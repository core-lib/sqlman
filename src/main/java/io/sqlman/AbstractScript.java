package io.sqlman;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 可执行脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:24
 */
public abstract class AbstractScript implements SqlScript {

    @Override
    public int execute(Connection connection, int ordinal) throws SQLException {
        SqlStatement statement = statement(ordinal);
        return statement.execute(connection);
    }

    /**
     * 获取该脚本指定序号的SQL语句
     *
     * @param ordinal SQL语句序号
     * @return 指定序号的SQL语句
     * @throws IndexOutOfBoundsException 序号超出边界时抛出
     */
    protected abstract SqlStatement statement(int ordinal) throws IndexOutOfBoundsException;
}
