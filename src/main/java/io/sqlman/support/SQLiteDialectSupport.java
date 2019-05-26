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
        ddl.append("     ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        ddl.append("     NAME VARCHAR(225) NOT NULL,");
        ddl.append("     VERSION VARCHAR(24) NOT NULL,");
        ddl.append("     ORDINAL INT(11) NOT NULL,");
        ddl.append("     DESCRIPTION VARCHAR(128) NOT NULL,");
        ddl.append("     SQL_QUANTITY INT(11) NOT NULL,");
        ddl.append("     SUCCESS BIT(1) NOT NULL,");
        ddl.append("     ROW_EFFECTED INT(11) NOT NULL,");
        ddl.append("     ERROR_CODE INT(11) NOT NULL,");
        ddl.append("     ERROR_STATE VARCHAR(255) NOT NULL,");
        ddl.append("     ERROR_MESSAGE VARCHAR(255) NOT NULL,");
        ddl.append("     TIME_EXECUTED TIMESTAMP NOT NULL DEFAULT (DATETIME('NOW', 'LOCALTIME'))");
        ddl.append(" )");
        PreparedStatement statement = connection.prepareStatement(ddl.toString());
        statement.execute();
    }

}
