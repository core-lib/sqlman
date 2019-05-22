package io.sqlman.resolver;

import io.sqlman.SqlResolver;
import io.sqlman.SqlScript;
import io.sqlman.SqlStatement;
import io.sqlman.SqlType;
import io.sqlman.utils.Laziness;
import io.sqlman.utils.Nullable;
import io.sqlman.utils.Sqls;
import io.sqlman.utils.Supplier;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.Provider;
import net.sf.jsqlparser.parser.StreamProvider;
import net.sf.jsqlparser.statement.Statement;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 基于URL的SQL脚本解析
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:42
 */
public class URLResolver implements SqlResolver<URL>, Supplier<SqlScript, URL> {
    private static final String SQL = ".sql";

    @Override
    public boolean validate(URL source) {
        String path = source.getPath();
        String extension = Sqls.getExtension(path);
        return SQL.equalsIgnoreCase(extension);
    }

    @Override
    public int contrast(URL source, String version) {
        String ver = Sqls.getVersion(source.getPath());
        return Sqls.compare(ver, version);
    }

    @Override
    public Enumeration<SqlScript> resolve(URL source) {
        return Laziness.forSingle(source, this);
    }

    @Override
    public Nullable<SqlScript> supply(URL source) throws Exception {
        String path = source.toURI().getPath();
        String version = Sqls.getVersion(path);
        String description = Sqls.getDescription(path);
        try (InputStream in = source.openStream()) {
            Provider provider = new StreamProvider(in);
            CCJSqlParser parser = new CCJSqlParser(provider);
            List<Statement> sqls = parser.Statements().getStatements();
            List<SqlStatement> statements = new ArrayList<>(sqls.size());
            for (int i = 0; i < sqls.size(); i++) {
                Statement sql = sqls.get(i);
                SqlStatement statement = new URLStatement(SqlType.DDL, i, sql.toString());
                statements.add(statement);
            }
            SqlScript script = new URLScript(version, description, statements);
            return Nullable.of(script);
        }
    }

    @Override
    public int compare(URL self, URL that) {
        String a = Sqls.getVersion(self.getPath());
        String b = Sqls.getVersion(that.getPath());
        return Sqls.compare(a, b);
    }

}
