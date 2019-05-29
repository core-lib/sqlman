package io.sqlman.dialect;

import io.sqlman.SqlDialectSupport;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Oracle方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/26 10:19
 */
public class OracleDialectSupport extends AbstractDialectSupport implements SqlDialectSupport {

    public OracleDialectSupport() {
    }

    public OracleDialectSupport(String table) {
        super(table);
    }

    @Override
    public void create(Connection connection) throws SQLException {
        StringBuilder ddl = new StringBuilder();

        ddl.append(" DECLARE EXISTED NUMBER;");
        ddl.append(" BEGIN");
        ddl.append("   SELECT COUNT(1) INTO EXISTED FROM USER_TABLES WHERE TABLE_NAME = UPPER('").append(table.toUpperCase()).append("');");
        ddl.append("   IF EXISTED = 0");
        ddl.append("   THEN");
        ddl.append("     EXECUTE IMMEDIATE");
        ddl.append("     'CREATE TABLE ").append(table.toUpperCase()).append(" (");
        ddl.append("       ID            INT          NOT NULL PRIMARY KEY,");
        ddl.append("       NAME          VARCHAR(255) NOT NULL,");
        ddl.append("       VERSION       VARCHAR(24)  NOT NULL,");
        ddl.append("       ORDINAL       INT          NOT NULL,");
        ddl.append("       DESCRIPTION   VARCHAR(128) NOT NULL,");
        ddl.append("       SQL_QUANTITY  INT          NOT NULL,");
        ddl.append("       SUCCESS       SMALLINT     NOT NULL,");
        ddl.append("       ROW_EFFECTED  INT          NOT NULL,");
        ddl.append("       ERROR_CODE    INT          NOT NULL,");
        ddl.append("       ERROR_STATE   VARCHAR(255) NOT NULL,");
        ddl.append("       ERROR_MESSAGE VARCHAR(255) NOT NULL,");
        ddl.append("       TIME_EXECUTED DATE         NOT NULL");
        ddl.append("     )';");
        ddl.append("     EXECUTE IMMEDIATE");
        ddl.append("     'CREATE SEQUENCE ").append(table.toUpperCase()).append("_SEQUENCE");
        ddl.append("       INCREMENT BY 1");
        ddl.append("       START WITH 1");
        ddl.append("       NOMAXVALUE");
        ddl.append("       NOMINVALUE");
        ddl.append("       NOCACHE';");
        ddl.append("     EXECUTE IMMEDIATE");
        ddl.append("     'CREATE OR REPLACE TRIGGER ").append(table.toUpperCase()).append("_TRIGGER");
        ddl.append("       BEFORE INSERT");
        ddl.append("       ON ").append(table.toUpperCase());
        ddl.append("       FOR EACH ROW");
        ddl.append("       BEGIN");
        ddl.append("         SELECT ").append(table.toUpperCase()).append("_SEQUENCE.NEXTVAL INTO :NEW.ID FROM DUAL;");
        ddl.append("       END;';");
        ddl.append("   END IF;");
        ddl.append(" END;");

        connection.prepareStatement(ddl.toString()).executeUpdate();
    }

    @Override
    public void remove(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TRIGGER " + table.toUpperCase() + "_TRIGGER").executeUpdate();
        connection.prepareStatement("DROP SEQUENCE " + table.toUpperCase() + "_SEQUENCE").executeUpdate();
        super.remove(connection);
    }
}
