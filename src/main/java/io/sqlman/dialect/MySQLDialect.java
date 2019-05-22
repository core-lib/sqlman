package io.sqlman.dialect;

import io.sqlman.SqlConfig;
import io.sqlman.SqlDialect;
import io.sqlman.SqlVersion;
import io.sqlman.utils.Sqls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL方言
 *
 * @author Payne 646742615@qq.com
 * 2019/5/21 17:19
 */
public class MySQLDialect implements SqlDialect {

    @Override
    public void install(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getTableName();
        StringBuilder ddl = new StringBuilder();
        ddl.append(" CREATE TABLE IF NOT EXISTS `").append(name).append("` (");
        ddl.append("         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '脚本执行记录ID',");
        ddl.append("         `version` varchar(24) NOT NULL COMMENT '脚本版本号',");
        ddl.append("         `ordinal` int(11) NOT NULL COMMENT '脚本SQL序号',");
        ddl.append("         `description` varchar(128) NOT NULL COMMENT '脚本描述',");
        ddl.append("         `sql_quantity` int(11) NOT NULL COMMENT '脚本SQL数量',");
        ddl.append("         `success` bit(1) NOT NULL COMMENT '是否执行成功',");
        ddl.append("         `row_effected` int(11) NOT NULL COMMENT '影响行数',");
        ddl.append("         `error_code` int(11) NOT NULL COMMENT '错误代码',");
        ddl.append("         `error_state` varchar(255) NOT NULL COMMENT '错误状态',");
        ddl.append("         `time_executed` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '执行时间',");
        ddl.append("         PRIMARY KEY (`id`),");
        ddl.append("         UNIQUE KEY `UK_").append(name).append("_version_ordinal` (`version`,`ordinal`) USING BTREE");
        ddl.append(" )");
        PreparedStatement statement = connection.prepareStatement(ddl.toString());
        statement.execute();
    }

    @Override
    public SqlVersion status(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getTableName();
        StringBuilder dql = new StringBuilder();
        dql.append(" SELECT");
        dql.append("     s.id AS id,");
        dql.append("     s.version AS version,");
        dql.append("     s.ordinal AS ordinal,");
        dql.append("     s.description AS description,");
        dql.append("     s.sql_quantity AS sqlQuantity,");
        dql.append("     s.success AS success,");
        dql.append("     s.row_effected AS rowEffected,");
        dql.append("     s.error_code AS errorCode,");
        dql.append("     s.error_state AS errorState,");
        dql.append("     s.time_executed AS timeExecuted");
        dql.append(" FROM");
        dql.append("     ").append(name).append(" AS s");
        dql.append(" ORDER BY");
        dql.append("     s.id DESC");
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
        version.setTimeExecuted(result.getTimestamp("timeExecuted"));

        return version;
    }

    @Override
    public int upgrade(Connection connection, SqlConfig config, SqlVersion version) throws SQLException {
        String name = config.getTableName();
        StringBuilder dml = new StringBuilder();
        dml.append(" INSERT INTO ").append(name).append(" (");
        dml.append("     version,");
        dml.append("     ordinal,");
        dml.append("     description,");
        dml.append("     sql_quantity,");
        dml.append("     success,");
        dml.append("     row_effected,");
        dml.append("     error_code,");
        dml.append("     error_state,");
        dml.append("     time_executed");
        dml.append(" )");
        dml.append(" VALUES");
        dml.append("     (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        dml.append(" ON DUPLICATE KEY UPDATE");
        dml.append("     description = ?,");
        dml.append("     sql_quantity = ?,");
        dml.append("     success = ?,");
        dml.append("     row_effected = ?,");
        dml.append("     error_code = ?,");
        dml.append("     error_state = ?,");
        dml.append("     time_executed = ?");

        PreparedStatement statement = connection.prepareStatement(dml.toString());
        statement.setString(1, Sqls.truncate(version.getVersion(), 24));
        statement.setInt(2, version.getOrdinal());
        statement.setString(3, Sqls.truncate(version.getDescription(), 128));
        statement.setInt(4, version.getSqlQuantity());
        statement.setBoolean(5, version.getSuccess());
        statement.setInt(6, version.getRowEffected());
        statement.setInt(7, version.getErrorCode());
        statement.setString(8, Sqls.truncate(version.getErrorState(), 255));
        statement.setTimestamp(9, version.getTimeExecuted());
        statement.setString(10, Sqls.truncate(version.getDescription(), 128));
        statement.setInt(11, version.getSqlQuantity());
        statement.setBoolean(12, version.getSuccess());
        statement.setInt(13, version.getRowEffected());
        statement.setInt(14, version.getErrorCode());
        statement.setString(15, Sqls.truncate(version.getErrorState(), 255));
        statement.setTimestamp(16, version.getTimeExecuted());

        return statement.executeUpdate();
    }
}
