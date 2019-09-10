package io.sqlman.core;

/**
 * SQL日志记录器供应商
 *
 * @author Payne 646742615@qq.com
 * 2019/5/27 11:03
 */
public interface SqlLoggerSupplier {

    /**
     * 日志记录器供应
     *
     * @param clazz 日志记录器所属类
     * @return 日志记录器
     */
    SqlLogger supply(Class<?> clazz);

    /**
     * 日志记录器供应
     *
     * @param name 日志记录器名称
     * @return 日志记录器
     */
    SqlLogger supply(String name);

}
