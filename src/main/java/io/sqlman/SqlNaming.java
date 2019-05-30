package io.sqlman;

import java.util.Objects;
import java.util.Set;

/**
 * SQL脚本信息
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 20:48
 */
public class SqlNaming {
    private final String name;
    private final String version;
    private final Set<String> parameters;
    private final String description;

    public SqlNaming(String name, String version, Set<String> parameters, String description) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        this.name = name;
        this.version = version;
        this.parameters = parameters;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Set<String> getParameters() {
        return parameters;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlNaming sqlNaming = (SqlNaming) o;
        return Objects.equals(version, sqlNaming.version);
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
