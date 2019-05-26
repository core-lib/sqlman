package io.sqlman.support;

import io.sqlman.SqlVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        ddl.append("   ID            INT          NOT NULL IDENTITY(1, 1),");
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
        ddl.append("   TIME_EXECUTED DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,");
        ddl.append("   PRIMARY KEY (ID)");
        ddl.append(" )");
        PreparedStatement statement = connection.prepareStatement(ddl.toString());
        statement.execute();
    }

    @Override
    public SqlVersion detect(Connection connection) throws SQLException {
        StringBuilder dql = new StringBuilder();
        dql.append(" SELECT");
        dql.append("     ID AS id,");
        dql.append("     NAME AS name,");
        dql.append("     VERSION AS version,");
        dql.append("     ORDINAL AS ordinal,");
        dql.append("     DESCRIPTION AS description,");
        dql.append("     SQL_QUANTITY AS sqlQuantity,");
        dql.append("     SUCCESS AS success,");
        dql.append("     ROW_EFFECTED AS rowEffected,");
        dql.append("     ERROR_CODE AS errorCode,");
        dql.append("     ERROR_STATE AS errorState,");
        dql.append("     ERROR_MESSAGE AS errorMessage,");
        dql.append("     TIME_EXECUTED AS timeExecuted");
        dql.append(" FROM");
        dql.append("     ").append(table.toUpperCase());
        dql.append(" ORDER BY");
        dql.append("     ID DESC");
        dql.append(" OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY");

        PreparedStatement statement = connection.prepareStatement(dql.toString());
        ResultSet result = statement.executeQuery();
        // 一条数据也没有
        if (!result.next()) {
            return null;
        }

        SqlVersion version = new SqlVersion();

        version.setId(result.getInt("id"));
        version.setName(result.getString("name"));
        version.setVersion(result.getString("version"));
        version.setOrdinal(result.getInt("ordinal"));
        version.setDescription(result.getString("description"));
        version.setSqlQuantity(result.getInt("sqlQuantity"));
        version.setSuccess(result.getBoolean("success"));
        version.setRowEffected(result.getInt("rowEffected"));
        version.setErrorCode(result.getInt("errorCode"));
        version.setErrorState(result.getString("errorState"));
        version.setErrorMessage(result.getString("errorMessage"));
        version.setTimeExecuted(result.getTimestamp("timeExecuted"));

        return version;
    }
}
