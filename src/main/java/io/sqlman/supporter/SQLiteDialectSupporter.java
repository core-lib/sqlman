package io.sqlman.supporter;

import io.sqlman.SqlConfig;
import io.sqlman.SqlUtils;
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
public class SQLiteDialectSupporter extends AbstractDialectSupporter implements SqlDialectSupporter {

    @Override
    public void install(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getName();
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

    @Override
    public void record(Connection connection, SqlConfig config, SqlVersion version) throws SQLException {
        String name = config.getName();
        StringBuilder dml = new StringBuilder();
        dml.append(" REPLACE INTO ").append(name).append(" (");
        dml.append("     version,");
        dml.append("     ordinal,");
        dml.append("     description,");
        dml.append("     sql_quantity,");
        dml.append("     success,");
        dml.append("     row_effected,");
        dml.append("     error_code,");
        dml.append("     error_state,");
        dml.append("     error_message,");
        dml.append("     time_executed");
        dml.append(" )");
        dml.append(" VALUES");
        dml.append("     (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement statement = connection.prepareStatement(dml.toString());
        statement.setString(1, SqlUtils.truncate(version.getVersion(), 24));
        statement.setInt(2, version.getOrdinal());
        statement.setString(3, SqlUtils.truncate(version.getDescription(), 128));
        statement.setInt(4, version.getSqlQuantity());
        statement.setBoolean(5, version.getSuccess());
        statement.setInt(6, version.getRowEffected());
        statement.setInt(7, version.getErrorCode());
        statement.setString(8, SqlUtils.truncate(version.getErrorState(), 255));
        statement.setString(9, SqlUtils.truncate(version.getErrorMessage(), 255));
        statement.setTimestamp(10, version.getTimeExecuted());

        statement.executeUpdate();
    }

    @Override
    public void lock(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getName();
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + name + "_lock (nil INTEGER PRIMARY KEY)");
        statement.execute();
    }

    @Override
    public void unlock(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getName();
        PreparedStatement statement = connection.prepareStatement("DROP TABLE " + name + "_lock");
        statement.execute();
    }
}
