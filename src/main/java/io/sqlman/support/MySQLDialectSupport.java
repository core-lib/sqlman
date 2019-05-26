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
        ddl.append(" CREATE TABLE IF NOT EXISTS `").append(table).append("` (");
        ddl.append("         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '脚本执行记录ID',");
        ddl.append("         `name` varchar(255) NOT NULL COMMENT '脚本名称',");
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

}
