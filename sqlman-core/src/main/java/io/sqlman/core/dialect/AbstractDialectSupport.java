package io.sqlman.core.dialect;

import io.sqlman.core.SqlDialectSupport;
import io.sqlman.core.SqlScript;
import io.sqlman.core.SqlSentence;
import io.sqlman.core.SqlVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.sqlman.core.SqlUtils.*;

/**
 * 抽象的数据库方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 9:52
 */
public abstract class AbstractDialectSupport implements SqlDialectSupport {
    protected String table = "SCHEMA_VERSION";

    protected AbstractDialectSupport() {
    }

    protected AbstractDialectSupport(String table) {
        if (table == null || table.trim().isEmpty()) {
            throw new IllegalArgumentException("table must not be null or blank string");
        }
        this.table = table;
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
        dql.append("     ").append(table);
        dql.append(" WHERE");
        dql.append("     ID = (SELECT MAX(ID) FROM ").append(table).append(")");

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
        dml.append("     NAME,");
        dml.append("     VERSION,");
        dml.append("     ORDINAL,");
        dml.append("     DESCRIPTION,");
        dml.append("     SQL_QUANTITY,");
        dml.append("     SUCCESS,");
        dml.append("     ROW_EFFECTED,");
        dml.append("     ERROR_CODE,");
        dml.append("     ERROR_STATE,");
        dml.append("     ERROR_MESSAGE,");
        dml.append("     TIME_EXECUTED");
        dml.append(" )");
        dml.append(" VALUES");
        dml.append("     (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement statement = connection.prepareStatement(dml.toString());
        statement.setString(1, truncate(version.getName(), 225));
        statement.setString(2, truncate(version.getVersion(), 24));
        statement.setInt(3, version.getOrdinal());
        statement.setString(4, truncate(version.getDescription(), 128));
        statement.setInt(5, version.getSqlQuantity());
        statement.setBoolean(6, version.getSuccess());
        statement.setInt(7, version.getRowEffected());
        statement.setInt(8, version.getErrorCode());
        statement.setString(9, truncate(version.getErrorState(), 255));
        statement.setString(10, truncate(version.getErrorMessage(), 255));
        statement.setTimestamp(11, version.getTimeExecuted());

        statement.executeUpdate();
    }

    @Override
    public void remove(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TABLE " + table + "").executeUpdate();
    }

    @Override
    public void lockup(Connection connection) throws SQLException {
        connection.prepareStatement("CREATE TABLE " + table + "_LOCK (NIL INTEGER)").executeUpdate();
    }

    @Override
    public void unlock(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TABLE " + table + "_LOCK").executeUpdate();
    }

    @Override
    public void backup(Connection connection, SqlScript script, int ordinal) throws SQLException {
        final SqlSentence sentence = script.sentence(ordinal);
        final String schema = sentence.schema();
        final String table = sentence.table();
        final String name = isEmpty(schema) ? table : schema + "." + table;
        if (table == null || table.trim().isEmpty()) {
            return;
        }
        try {
            connection.prepareStatement("SELECT COUNT(*) FROM " + name).executeQuery();
        } catch (SQLException e) {
            return;
        }
        final String backup = unwrap(table) + "_bak_" + script.version().replace('.', '_') + "$" + ordinal;
        final String bak = isEmpty(schema) ? backup : schema + "." + backup;
        connection.prepareStatement("CREATE TABLE " + bak + " AS SELECT * FROM " + name).executeUpdate();
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
