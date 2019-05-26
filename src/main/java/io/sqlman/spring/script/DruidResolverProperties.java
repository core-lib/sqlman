package io.sqlman.spring.script;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * druid脚本资源解析器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:28
 */
@ConfigurationProperties(prefix = "sqlman.script")
public class DruidResolverProperties extends AbstractResolverProperties {
    private String dialect = "MySQL";
    private String charset = "UTF-8";

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
