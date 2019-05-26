package io.sqlman.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQLite方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/23 15:52
 */
public class SQLiteDialectSupport extends AbstractDialectSupport implements SqlDialectSupport {

    public SQLiteDialectSupport() {
    }

    public SQLiteDialectSupport(String table) {
        super(table.toUpperCase());
    }

    @Override
    public void create(Connection connection) throws SQLException {
        StringBuilder ddl = new StringBuilder();
        ddl.append(" CREATE TABLE IF NOT EXISTS ").append(table.toUpperCase()).append(" (");
        ddl.append("     id integer NOT NULL PRIMARY KEY AUTOINCREMENT,");
        ddl.append("     name varchar (225) NOT NULL,");
        ddl.append("     version varchar (24) NOT NULL,");
        ddl.append("     ordinal int (11) NOT NULL,");
        ddl.append("     description varchar (128) NOT NULL,");
        ddl.append("     sql_quantity int (11) NOT NULL,");
        ddl.append("     success bit (1) NOT NULL,");
        ddl.append("     row_effected int (11) NOT NULL,");
        ddl.append("     error_code int (11) NOT NULL,");
        ddl.append("     error_state varchar (255) NOT NULL,");
        ddl.append("     error_message varchar (255) NOT NULL,");
        ddl.append("     time_executed timestamp NOT NULL DEFAULT (datetime('now', 'localtime'))");
        ddl.append(" )");
        PreparedStatement statement = connection.prepareStatement(ddl.toString());
        statement.execute();
    }

}
