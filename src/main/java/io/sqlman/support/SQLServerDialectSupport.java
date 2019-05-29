package io.sqlman.support;

import io.sqlman.SqlDialectSupport;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQLServer方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/26 9:08
 */
public class SQLServerDialectSupport extends AbstractDialectSupport implements SqlDialectSupport {

    public SQLServerDialectSupport() {
    }

    public SQLServerDialectSupport(String table) {
        super(table.toUpperCase());
    }

    @Override
    public void create(Connection connection) throws SQLException {
        StringBuilder ddl = new StringBuilder();

        ddl.append(" IF NOT EXISTS(");
        ddl.append("     SELECT *");
        ddl.append("     FROM SYSOBJECTS");
        ddl.append("     WHERE ID = OBJECT_ID('").append(table.toUpperCase()).append("')");
        ddl.append(" )");
        ddl.append(" CREATE TABLE ").append(table.toUpperCase()).append(" (");
        ddl.append("   ID            INT          NOT NULL PRIMARY KEY IDENTITY(1, 1),");
        ddl.append("   NAME          VARCHAR(255) NOT NULL,");
        ddl.append("   VERSION       VARCHAR(24)  NOT NULL,");
        ddl.append("   ORDINAL       INT          NOT NULL,");
        ddl.append("   DESCRIPTION   VARCHAR(128) NOT NULL,");
        ddl.append("   SQL_QUANTITY  INT          NOT NULL,");
        ddl.append("   SUCCESS       BIT          NOT NULL,");
        ddl.append("   ROW_EFFECTED  INT          NOT NULL,");
        ddl.append("   ERROR_CODE    INT          NOT NULL,");
        ddl.append("   ERROR_STATE   VARCHAR(255) NOT NULL,");
        ddl.append("   ERROR_MESSAGE VARCHAR(255) NOT NULL,");
        ddl.append("   TIME_EXECUTED DATETIME     NOT NULL");
        ddl.append(" )");

        connection.prepareStatement(ddl.toString()).executeUpdate();
    }

}
