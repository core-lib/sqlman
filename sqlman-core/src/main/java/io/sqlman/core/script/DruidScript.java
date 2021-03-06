package io.sqlman.core.script;

import io.sqlman.core.SqlScript;
import io.sqlman.core.SqlSentence;

import java.util.*;

/**
 * 基于druid的SQL脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:44
 */
public class DruidScript implements SqlScript {
    private final String name;
    private final String version;
    private final Set<String> instructions;
    private final String description;
    private final List<SqlSentence> sentences;

    public DruidScript(String name, String version, Set<String> instructions, String description, List<SqlSentence> sentences) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("version must not be null or empty string");
        }
        if (sentences == null || sentences.isEmpty()) {
            throw new IllegalArgumentException("sentences must not be null or empty list");
        }
        this.name = name;
        this.version = version;
        this.instructions = instructions;
        this.description = description;
        this.sentences = sentences;
    }

    @Override
    public int sqls() {
        return sentences.size();
    }

    @Override
    public SqlSentence sentence(int ordinal) {
        return sentences.get(ordinal - 1);
    }

    @Override
    public Enumeration<SqlSentence> sentences() {
        return Collections.enumeration(sentences);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public Set<String> instructions() {
        return instructions;
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
        DruidScript that = (DruidScript) o;
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
