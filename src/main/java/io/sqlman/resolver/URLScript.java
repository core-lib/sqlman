package io.sqlman.resolver;

import io.sqlman.SqlScript;
import io.sqlman.SqlStatement;
import io.sqlman.script.ExecutableScript;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 基于URL的SQL脚本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:44
 */
public class URLScript extends ExecutableScript implements SqlScript {
    private final String version;
    private final String description;
    private final List<SqlStatement> statements;

    public URLScript(String version, String description, List<SqlStatement> statements) {
        this.version = version;
        this.description = description;
        this.statements = statements;
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

}
