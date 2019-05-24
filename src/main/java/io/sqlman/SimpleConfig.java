package io.sqlman;

/**
 * 框架配置
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:25
 */
public class SimpleConfig implements SqlConfig {
    private String name = "sqlman";
    private SqlIsolation isolation = SqlIsolation.DEFAULT;
    private String location = "sqlman/**/*.sql";
    private String charset = "UTF-8";

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SqlIsolation getIsolation() {
        return isolation;
    }

    public void setIsolation(SqlIsolation isolation) {
        this.isolation = isolation;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
