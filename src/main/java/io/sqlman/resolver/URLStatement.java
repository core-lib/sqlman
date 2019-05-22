package io.sqlman.resolver;

import io.sqlman.SqlStatement;
import io.sqlman.SqlType;
import io.sqlman.statement.ExecutableStatement;

/**
 * 基于URL的SQL脚本语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:15
 */
public class URLStatement extends ExecutableStatement implements SqlStatement {
    private final SqlType type;
    private final int index;
    private final String statement;

    public URLStatement(SqlType type, int index, String statement) {
        this.type = type;
        this.index = index;
        this.statement = statement;
    }

    @Override
    public SqlType type() {
        return type;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public String statement() {
        return statement;
    }
}
