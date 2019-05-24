package io.sqlman.spring;

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
    private ScriptConfigProperties script = new ScriptConfigProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ScriptConfigProperties getScript() {
        return script;
    }

    public void setScript(ScriptConfigProperties script) {
        this.script = script;
    }

    public static class ScriptConfigProperties {
        private String location = "sqlman/**/*.sql";
        private String charset = "UTF-8";

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
    }

}
