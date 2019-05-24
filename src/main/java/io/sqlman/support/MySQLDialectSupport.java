package io.sqlman.support;

import io.sqlman.SqlConfig;
import io.sqlman.SqlUtils;
import io.sqlman.SqlVersion;

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

    @Override
    public void install(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getName();
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
        ddl.append("         `error_message` varchar(255) NOT NULL COMMENT '错误信息',");
        ddl.append("         `time_executed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '执行时间',");
        ddl.append("         PRIMARY KEY (`id`)");
        ddl.append(" )");
        PreparedStatement statement = connection.prepareStatement(ddl.toString());
        statement.execute();
    }

    @Override
    public void record(Connection connection, SqlConfig config, SqlVersion version) throws SQLException {
        String name = config.getName();
        StringBuilder dml = new StringBuilder();
        dml.append(" INSERT INTO `").append(name).append("` (");
        dml.append("     version,");
        dml.append("     ordinal,");
        dml.append("     description,");
        dml.append("     sql_quantity,");
        dml.append("     success,");
        dml.append("     row_effected,");
        dml.append("     error_code,");
        dml.append("     error_state,");
        dml.append("     error_message,");
        dml.append("     time_executed");
        dml.append(" )");
        dml.append(" VALUES");
        dml.append("     (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        dml.append(" ON DUPLICATE KEY UPDATE");
        dml.append("     description = ?,");
        dml.append("     sql_quantity = ?,");
        dml.append("     success = ?,");
        dml.append("     row_effected = ?,");
        dml.append("     error_code = ?,");
        dml.append("     error_state = ?,");
        dml.append("     error_message = ?,");
        dml.append("     time_executed = ?");

        PreparedStatement statement = connection.prepareStatement(dml.toString());
        statement.setString(1, SqlUtils.truncate(version.getVersion(), 24));
        statement.setInt(2, version.getOrdinal());
        statement.setString(3, SqlUtils.truncate(version.getDescription(), 128));
        statement.setInt(4, version.getSqlQuantity());
        statement.setBoolean(5, version.getSuccess());
        statement.setInt(6, version.getRowEffected());
        statement.setInt(7, version.getErrorCode());
        statement.setString(8, SqlUtils.truncate(version.getErrorState(), 255));
        statement.setString(9, SqlUtils.truncate(version.getErrorMessage(), 255));
        statement.setTimestamp(10, version.getTimeExecuted());
        statement.setString(11, SqlUtils.truncate(version.getDescription(), 128));
        statement.setInt(12, version.getSqlQuantity());
        statement.setBoolean(13, version.getSuccess());
        statement.setInt(14, version.getRowEffected());
        statement.setInt(15, version.getErrorCode());
        statement.setString(16, SqlUtils.truncate(version.getErrorState(), 255));
        statement.setString(17, SqlUtils.truncate(version.getErrorMessage(), 255));
        statement.setTimestamp(18, version.getTimeExecuted());

        statement.executeUpdate();
    }

    @Override
    public void lock(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getName();
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + name + "_lock (nil INT(1) PRIMARY KEY)");
        statement.execute();
    }

    @Override
    public void unlock(Connection connection, SqlConfig config) throws SQLException {
        String name = config.getName();
        PreparedStatement statement = connection.prepareStatement("DROP TABLE " + name + "_lock");
        statement.execute();
    }
}
