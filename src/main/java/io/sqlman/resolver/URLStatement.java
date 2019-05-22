package io.sqlman.resolver;

import io.sqlman.SqlStatement;
import io.sqlman.statement.ExecutableStatement;

/**
 * 基于URL的SQL脚本语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:15
 */
public class URLStatement extends ExecutableStatement implements SqlStatement {
    private final int ordinal;
    private final String statement;

    public URLStatement(int ordinal, String statement) {
        this.ordinal = ordinal;
        this.statement = statement;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String statement() {
        return statement;
    }
}
