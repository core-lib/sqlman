package io.sqlman.dialect;

import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.SqlConfig;
import io.sqlman.SqlDialect;
import io.sqlman.SqlVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQLite方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/23 15:52
 */
public class SQLiteDialect implements SqlDialect {
    @Override
    public String type() {
        return JdbcUtils.SQLITE;
    }

    @Override
    public void install(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getTableName();
        {
            StringBuilder ddl = new StringBuilder();
            ddl.append(" CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
            ddl.append("     id integer NOT NULL PRIMARY KEY AUTOINCREMENT,");
            ddl.append("     version varchar (24) NOT NULL,");
            ddl.append("     ordinal int (11) NOT NULL,");
            ddl.append("     description varchar (128) NOT NULL,");
            ddl.append("     sql_quantity int (11) NOT NULL,");
            ddl.append("     success bit (1) NOT NULL,");
            ddl.append("     row_effected int (11) NOT NULL,");
            ddl.append("     error_code int (11) NOT NULL,");
            ddl.append("     error_state varchar (255) NOT NULL,");
            ddl.append("     error_message varchar (255) NOT NULL,");
            ddl.append("     time_executed timestamp NOT NULL DEFAULT (");
            ddl.append("         datetime('now', 'localtime')");
            ddl.append("     )");
            ddl.append(" )");
            PreparedStatement statement = connection.prepareStatement(ddl.toString());
            statement.execute();
        }
        {
            String sql = "CREATE UNIQUE INDEX UK_" + name + "_version_ordinal ON " + name + " (version DESC, ordinal DESC)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
        }
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
