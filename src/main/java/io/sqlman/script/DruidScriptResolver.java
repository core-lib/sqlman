package io.sqlman.script;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.util.JdbcUtils;
import io.sqlman.*;
import io.sqlman.exception.IncorrectSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于druid的SQL脚本解析
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:42
 */
public class DruidScriptResolver implements SqlScriptResolver {
    private String dialect = JdbcUtils.MYSQL;
    private String charset = "UTF-8";

    public DruidScriptResolver() {
    }

    public DruidScriptResolver(String dialect) {
        if (dialect == null || dialect.trim().isEmpty()) {
            throw new IllegalArgumentException("dialect must not be null or blank string");
        }
        this.dialect = dialect;
    }

    public DruidScriptResolver(String dialect, String charset) {
        if (dialect == null || dialect.trim().isEmpty()) {
            throw new IllegalArgumentException("dialect must not be null or blank string");
        }
        if (charset == null || charset.trim().isEmpty()) {
            throw new IllegalArgumentException("charset must not be null or blank string");
        }
        this.dialect = dialect;
        this.charset = charset;
    }

    @Override
    public SqlScript resolve(SqlSource source) throws IncorrectSyntaxException, IOException {
        try (InputStream in = source.open()) {
            String text = SqlUtils.stringify(in, charset);
            List<SQLStatement> statements = SQLUtils.parseStatements(text, dialect.toLowerCase());
            List<SqlSentence> sentences = new ArrayList<>(statements.size());
            for (int index = 0; index < statements.size(); index++) {
                SQLStatement statement = statements.get(index);
                String sql = statement.toString();
                sql = sql.trim();
                String suffix = ";";
                while (sql.endsWith(suffix)) {
                    sql = sql.substring(0, sql.length() - 1);
                }
                String table = null;
                List<SQLObject> children = statement.getChildren();
                for (SQLObject child : children) {
                    if (child instanceof SQLExprTableSource) {
                        SQLExprTableSource tableSource = (SQLExprTableSource) child;
                        table = tableSource.getName().toString();
                        break;
                    }
                }
                SqlSentence sentence = new DruidSentence(index + 1, sql, table);
                sentences.add(sentence);
            }
            return new DruidScript(source.name(), source.version(), source.parameters(), source.description(), sentences);
        } catch (ParserException ex) {
            throw new IncorrectSyntaxException(ex);
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
