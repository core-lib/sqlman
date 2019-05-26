package io.sqlman.support;

import io.sqlman.SqlUtils;
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
    public SqlVersion detect(Connection connection) throws SQLException {
        StringBuilder dql = new StringBuilder();
        dql.append(" SELECT");
        dql.append("     `id` AS id,");
        dql.append("     `name` AS name,");
        dql.append("     `version` AS version,");
        dql.append("     `ordinal` AS ordinal,");
        dql.append("     `description` AS description,");
        dql.append("     `sql_quantity` AS sqlQuantity,");
        dql.append("     `success` AS success,");
        dql.append("     `row_effected` AS rowEffected,");
        dql.append("     `error_code` AS errorCode,");
        dql.append("     `error_state` AS errorState,");
        dql.append("     `error_message` AS errorMessage,");
        dql.append("     `time_executed` AS timeExecuted");
        dql.append(" FROM");
        dql.append("     `").append(table).append("`");
        dql.append(" ORDER BY");
        dql.append("     `id` DESC");
        dql.append(" LIMIT 0, 1");

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

    @Override
    public void update(Connection connection, SqlVersion version) throws SQLException {
        StringBuilder dml = new StringBuilder();
        dml.append(" INSERT INTO ").append(table).append(" (");
        dml.append("     `name`,");
        dml.append("     `version`,");
        dml.append("     `ordinal`,");
        dml.append("     `description`,");
        dml.append("     `sql_quantity`,");
        dml.append("     `success`,");
        dml.append("     `row_effected`,");
        dml.append("     `error_code`,");
        dml.append("     `error_state`,");
        dml.append("     `error_message`,");
        dml.append("     `time_executed`");
        dml.append(" )");
        dml.append(" VALUES");
        dml.append("     (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement statement = connection.prepareStatement(dml.toString());
        statement.setString(1, SqlUtils.truncate(version.getName(), 225));
        statement.setString(2, SqlUtils.truncate(version.getVersion(), 24));
        statement.setInt(3, version.getOrdinal());
        statement.setString(4, SqlUtils.truncate(version.getDescription(), 128));
        statement.setInt(5, version.getSqlQuantity());
        statement.setBoolean(6, version.getSuccess());
        statement.setInt(7, version.getRowEffected());
        statement.setInt(8, version.getErrorCode());
        statement.setString(9, SqlUtils.truncate(version.getErrorState(), 255));
        statement.setString(10, SqlUtils.truncate(version.getErrorMessage(), 255));
        statement.setTimestamp(11, version.getTimeExecuted());

        statement.executeUpdate();
    }

    @Override
    public void remove(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE TABLE `" + table + "`");
        statement.execute();
    }

    @Override
    public void lockup(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE `" + table + "_lock` (`nil` INTEGER PRIMARY KEY)");
        statement.execute();
    }

    @Override
    public void unlock(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DROP TABLE `" + table + "_lock`");
        statement.execute();
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
