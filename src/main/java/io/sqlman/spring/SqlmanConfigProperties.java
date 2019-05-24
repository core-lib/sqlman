package io.sqlman.spring;

import io.sqlman.manager.SqlIsolation;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 16:34
 */
@ConfigurationProperties(prefix = "sqlman")
public class SqlmanConfigProperties {
    private boolean enabled = true;
    private SqlIsolation isolation = SqlIsolation.DEFAULT;
    private String dialect = "mysql";
    private SqlmanScriptProperties script = new SqlmanScriptProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SqlIsolation getIsolation() {
        return isolation;
    }

    public void setIsolation(SqlIsolation isolation) {
        this.isolation = isolation;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public SqlmanScriptProperties getScript() {
        return script;
    }

    public void setScript(SqlmanScriptProperties script) {
        this.script = script;
    }

    public static class SqlmanScriptProperties {
        private String location;
        private String charset;
        private SqlmanNamingProperties naming = new SqlmanNamingProperties();

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public SqlmanNamingProperties getNaming() {
            return naming;
        }

        public void setNaming(SqlmanNamingProperties naming) {
            this.naming = naming;
        }
    }

    public static class SqlmanNamingProperties {
        private char separator = '/';
        private String delimiter = "-";
        private String extension = ".sql";

        public char getSeparator() {
            return separator;
        }

        public void setSeparator(char separator) {
            this.separator = separator;
        }

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }
    }
}
