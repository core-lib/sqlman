package io.sqlman;

/**
 * 基本SQL脚本语句
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:15
 */
public class BasicStatement implements SqlStatement {
    private final int ordinal;
    private final String statement;

    public BasicStatement(int ordinal, String statement) {
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

    @Override
    public String toString() {
        return statement;
    }
}
