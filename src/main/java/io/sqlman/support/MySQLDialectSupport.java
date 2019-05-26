package io.sqlman.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/21 17:19
 */
public class MySQLDialectSupport extends AbstractDialectSupport implements SqlDialectSupport {

    public MySQLDialectSupport() {
    }

    public MySQLDialectSupport(String table) {
        super(table);
    }

    @Override
    public void create(Connection connection) throws SQLException {
        StringBuilder ddl = new StringBuilder();
        ddl.append(" CREATE TABLE IF NOT EXISTS ").append(table).append(" (");
        ddl.append("     ID INT(11) NOT NULL AUTO_INCREMENT,");
        ddl.append("     NAME VARCHAR(255) NOT NULL,");
        ddl.append("     VERSION VARCHAR(24) NOT NULL,");
        ddl.append("     ORDINAL INT(11) NOT NULL,");
        ddl.append("     DESCRIPTION VARCHAR(128) NOT NULL,");
        ddl.append("     SQL_QUANTITY INT(11) NOT NULL,");
        ddl.append("     SUCCESS BIT(1) NOT NULL,");
        ddl.append("     ROW_EFFECTED INT(11) NOT NULL,");
        ddl.append("     ERROR_CODE INT(11) NOT NULL,");
        ddl.append("     ERROR_STATE VARCHAR(255) NOT NULL,");
        ddl.append("     ERROR_MESSAGE VARCHAR(255) NOT NULL,");
        ddl.append("     TIME_EXECUTED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,");
        ddl.append("     PRIMARY KEY (ID)");
        ddl.append(" )");
        PreparedStatement statement = connection.prepareStatement(ddl.toString());
        statement.execute();
    }

}
