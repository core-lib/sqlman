package io.sqlman.dialect;

import io.sqlman.SqlDialectSupport;
import io.sqlman.SqlScript;
import io.sqlman.SqlSentence;

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

    @Override
    public void backup(Connection connection, SqlScript script, int ordinal) throws SQLException {
        SqlSentence sentence = script.sentence(ordinal);
        String table = sentence.table();
        if (table == null || table.trim().isEmpty()) {
            return;
        }
        try {
            connection.prepareStatement("SELECT COUNT(*) FROM " + table).executeQuery();
        } catch (SQLException e) {
            return;
        }
        table = table + "_bak_" + script.version().replace('.', '_') + "$" + ordinal;
        connection.prepareStatement("SELECT * INTO " + table + " FROM " + sentence.table()).executeUpdate();
    }
}
