package io.sqlman;

import java.util.Objects;

/**
 * SQL脚本信息
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 20:48
 */
public class SqlInfo {
    private final String name;
    private final String version;
    private final String description;

    public SqlInfo(String name, String version, String description) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        this.name = name;
        this.version = version;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlInfo sqlInfo = (SqlInfo) o;
        return Objects.equals(version, sqlInfo.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return name;
    }
}
