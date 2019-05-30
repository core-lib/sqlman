package io.sqlman.version;

/**
 * JDBC的指令常量
 *
 * @author Payne 646742615@qq.com
 * 2019/5/30 15:04
 */
public interface JdbcInstruction {
    /**
     * 原子性执行：即整个脚本作为一个事务整体，不可分割。
     */
    String INSTRUCTION_ATOMIC = "ATOMIC";
    /**
     * 隔离级别：读未提交
     */
    String INSTRUCTION_READ_UNCOMMITTED = "READ_UNCOMMITTED";
    /**
     * 隔离级别：读已提交
     */
    String INSTRUCTION_READ_COMMITTED = "READ_COMMITTED";
    /**
     * 隔离级别：可重复读
     */
    String INSTRUCTION_REPEATABLE_READ = "REPEATABLE_READ";
    /**
     * 隔离级别：串行执行
     */
    String INSTRUCTION_SERIALIZABLE = "SERIALIZABLE";

}
