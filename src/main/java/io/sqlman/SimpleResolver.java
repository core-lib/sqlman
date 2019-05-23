package io.sqlman;

import io.sqlman.utils.Enumerations;
import io.sqlman.utils.Nullable;
import io.sqlman.utils.Sqls;
import io.sqlman.utils.Supplier;
import net.sf.jsqlparser.parser.CCJSqlParser;
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
public class SimpleResolver implements SqlResolver<URL>, Supplier<SqlScript, URL> {

    @Override
    public int contrast(URL source, String version) {
        String ver = Sqls.getVersion(source.getPath());
        return Sqls.compare(ver, version);
    }

    @Override
    public Enumeration<SqlScript> resolve(URL source) {
        return Enumerations.create(source, this);
    }

    @Override
    public Nullable<SqlScript> supply(URL source) throws Exception {
        String path = source.toURI().getPath();
        String version = Sqls.getVersion(path);
        String description = Sqls.getDescription(path);
        try (InputStream in = source.openStream()) {
            CCJSqlParser parser = new CCJSqlParser(in);
            List<Statement> sqls = parser.Statements().getStatements();
            List<SqlStatement> statements = new ArrayList<>(sqls.size());
            for (int ordinal = 0; ordinal < sqls.size(); ordinal++) {
                Statement sql = sqls.get(ordinal);
                SqlStatement statement = new BasicStatement(ordinal, sql.toString());
                statements.add(statement);
            }
            SqlScript script = new BasicScript(version, description, statements);
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
