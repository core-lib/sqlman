package io.sqlman;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import io.sqlman.utils.Sqls;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的SQL脚本解析
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:42
 */
public class SimpleResolver implements SqlResolver {

    @Override
    public int compare(SqlResource resource, String version) {
        String ver = Sqls.getVersion(resource.name());
        return Sqls.compare(ver, version);
    }

    @Override
    public SqlScript resolve(SqlResource resource, String dbType) throws Exception {
        String name = resource.name();
        String version = Sqls.getVersion(name);
        String description = Sqls.getDescription(name);
        try (InputStream in = resource.stream()) {
            String text = Sqls.stringify(in);
            List<SQLStatement> sqls = SQLUtils.parseStatements(text, dbType);
            List<SqlStatement> statements = new ArrayList<>(sqls.size());
            for (int ordinal = 0; ordinal < sqls.size(); ordinal++) {
                SQLStatement sql = sqls.get(ordinal);
                SqlStatement statement = new BasicStatement(ordinal, sql.toString());
                statements.add(statement);
            }
            return new BasicScript(version, description, statements);
        }
    }

    @Override
    public int compare(SqlResource self, SqlResource that) {
        String a = Sqls.getVersion(self.name());
        String b = Sqls.getVersion(that.name());
        return Sqls.compare(a, b);
    }

}
