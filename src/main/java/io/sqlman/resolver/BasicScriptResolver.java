package io.sqlman.resolver;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.SqlResource;
import io.sqlman.SqlScript;
import io.sqlman.SqlStatement;
import io.sqlman.SqlUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于druid的SQL脚本解析
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:42
 */
public class BasicScriptResolver implements SqlScriptResolver {
    private String dialect = JdbcUtils.MYSQL;
    private String charset = "UTF-8";

    public BasicScriptResolver() {
    }

    public BasicScriptResolver(String dialect, String charset) {
        this.dialect = dialect;
        this.charset = charset;
    }

    @Override
    public SqlScript resolve(SqlResource resource) throws Exception {
        try (InputStream in = resource.open()) {
            String text = SqlUtils.stringify(in, charset);
            List<SQLStatement> sqls = SQLUtils.parseStatements(text, dialect.toLowerCase());
            List<SqlStatement> statements = new ArrayList<>(sqls.size());
            for (int ordinal = 0; ordinal < sqls.size(); ordinal++) {
                SQLStatement sql = sqls.get(ordinal);
                SqlStatement statement = new BasicStatement(ordinal, sql.toString());
                statements.add(statement);
            }
            return new BasicScript(resource.name(), resource.version(), resource.description(), statements);
        }
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
