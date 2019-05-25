package io.sqlman.support;

import io.sqlman.SqlVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 抽象的数据库方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 9:52
 */
public abstract class AbstractDialectSupport implements SqlDialectSupport {
    protected String table = "sqlman";

    protected AbstractDialectSupport() {
    }

    protected AbstractDialectSupport(String table) {
        this.table = table;
    }

    @Override
    public SqlVersion examine(Connection connection) throws SQLException {
        StringBuilder dql = new StringBuilder();
        dql.append(" SELECT");
        dql.append("     id AS id,");
        dql.append("     version AS version,");
        dql.append("     ordinal AS ordinal,");
        dql.append("     description AS description,");
        dql.append("     sql_quantity AS sqlQuantity,");
        dql.append("     success AS success,");
        dql.append("     row_effected AS rowEffected,");
        dql.append("     error_code AS errorCode,");
        dql.append("     error_state AS errorState,");
        dql.append("     error_message AS errorMessage,");
        dql.append("     time_executed AS timeExecuted");
        dql.append(" FROM");
        dql.append("     `").append(table).append("`");
        dql.append(" ORDER BY");
        dql.append("     id DESC");
        dql.append(" LIMIT 0, 1");

        PreparedStatement statement = connection.prepareStatement(dql.toString());
        ResultSet result = statement.executeQuery();
        // 一条数据也没有
        if (!result.next()) {
            return null;
        }

        SqlVersion version = new SqlVersion();

        version.setId(result.getInt("id"));
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
