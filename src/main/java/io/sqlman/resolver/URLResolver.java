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
    public Enumeration<SqlScript> resolve(URL source) {
        return Laziness.forSingle(source, this);
    }

    @Override
    public Nullable<SqlScript> supply(URL source) throws Exception {
        String path = source.toURI().getPath();
        int index = path.lastIndexOf('/');
        String filename = index < 0 ? path : path.substring(index + 1);
        String name = Sqls.getName(filename);

        int idx = name.indexOf('-');

        String version = idx < 0 ? name : name.substring(0, idx);
        String description = idx < 0 ? "" : name.substring(idx + 1);
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
        try {
            String a = Sqls.getVersion(self.toURI().getPath());
            String b = Sqls.getVersion(that.toURI().getPath());
            String[] as = a.split("\\.");
            String[] bs = b.split("\\.");
            for (int i = 0; i < as.length && i < bs.length; i++) {
                int l = Integer.valueOf(as[i]);
                int r = Integer.valueOf(bs[i]);
                int comparision = Integer.compare(l, r);
                if (comparision != 0) {
                    return comparision;
                }
            }
            return Integer.compare(as.length, bs.length);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


}
