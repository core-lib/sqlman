package io.sqlman;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * 基本SQL脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:44
 */
public class BasicScript extends AbstractScript implements SqlScript {
    private final String version;
    private final String description;
    private final List<SqlStatement> statements;

    public BasicScript(String version, String description, List<SqlStatement> statements) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("version must not be null or empty string");
        }
        if (statements == null || statements.isEmpty()) {
            throw new IllegalArgumentException("statements must not be null or empty list");
        }
        this.version = version;
        this.description = description;
        this.statements = statements;
    }

    @Override
    protected SqlStatement statement(int ordinal) {
        return statements.get(ordinal);
    }

    @Override
    public int sqls() {
        return statements.size();
    }

    @Override
    public Enumeration<SqlStatement> statements() {
        return Collections.enumeration(statements);
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicScript that = (BasicScript) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return version;
    }
}
