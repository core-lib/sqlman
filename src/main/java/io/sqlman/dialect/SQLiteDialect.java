package io.sqlman.dialect;

import io.sqlman.SqlConfig;
import io.sqlman.SqlDialect;
import io.sqlman.SqlVersion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQLite方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/23 11:12
 */
public class SQLiteDialect implements SqlDialect {
    @Override
    public void install(Connection connection, SqlConfig config) throws SQLException {

    }

    @Override
    public SqlVersion status(Connection connection, SqlConfig config) throws SQLException {
        return null;
    }

    @Override
    public int upgrade(Connection connection, SqlConfig config, SqlVersion version) throws SQLException {
        return 0;
    }
}
