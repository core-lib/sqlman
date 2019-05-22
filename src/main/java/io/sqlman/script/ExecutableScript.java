package io.sqlman.script;

import io.sqlman.SqlScript;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 可执行脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:24
 */
public abstract class ExecutableScript implements SqlScript {

    @Override
    public void execute(Connection connection, int index) throws SQLException {

    }
}
