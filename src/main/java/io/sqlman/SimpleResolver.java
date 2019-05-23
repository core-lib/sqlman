package io.sqlman;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import io.sqlman.utils.Enumerations;
import io.sqlman.utils.Nullable;
import io.sqlman.utils.Sqls;
import io.sqlman.utils.Supplier;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 简单的SQL脚本解析
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:42
 */
public class SimpleResolver implements SqlResolver<URL> {
    private String charset = Charset.defaultCharset().name();

    @Override
    public int contrast(URL source, String version) {
        String ver = Sqls.getVersion(source.getPath());
        return Sqls.compare(ver, version);
    }

    @Override
    public Enumeration<SqlScript> resolve(URL source, String dbType) {
        return Enumerations.create(source, new SimpleSupplier(dbType, charset));
    }

    @Override
    public int compare(URL self, URL that) {
        String a = Sqls.getVersion(self.getPath());
        String b = Sqls.getVersion(that.getPath());
        return Sqls.compare(a, b);
    }

    private static class SimpleSupplier implements Supplier<SqlScript, URL> {
        private final String dbType;
        private final String charset;

        SimpleSupplier(String dbType, String charset) {
            this.dbType = dbType;
            this.charset = charset;
        }

        @Override
        public Nullable<SqlScript> supply(URL source) throws Exception {
            String path = source.toURI().getPath();
            String version = Sqls.getVersion(path);
            String description = Sqls.getDescription(path);
            try (InputStream in = source.openStream()) {
                String text = Sqls.stringify(in, charset);
                List<SQLStatement> sqls = SQLUtils.parseStatements(text, dbType);
                List<SqlStatement> statements = new ArrayList<>(sqls.size());
                for (int ordinal = 0; ordinal < sqls.size(); ordinal++) {
                    SQLStatement sql = sqls.get(ordinal);
                    SqlStatement statement = new BasicStatement(ordinal, sql.toString());
                    statements.add(statement);
                }
                SqlScript script = new BasicScript(version, description, statements);
                return Nullable.of(script);
            }
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
