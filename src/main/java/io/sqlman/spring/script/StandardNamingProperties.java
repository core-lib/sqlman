package io.sqlman.spring.script;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础脚本资源命名策略配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 8:54
 */
@ConfigurationProperties(prefix = "sqlman.script.naming")
public class StandardNamingProperties extends AbstractNamingProperties {
    /**
     * SQL script name separator
     */
    private char separator = '/';
    /**
     * SQL script name delimiter
     */
    private String delimiter = "-";
    /**
     * SQL script name extension
     */
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
