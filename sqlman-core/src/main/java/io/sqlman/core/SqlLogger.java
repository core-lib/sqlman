package io.sqlman.core;

/**
 * SQL日志记录器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 17:59
 */
public interface SqlLogger {

    /**
     * 获取日志记录器名称
     *
     * @return 日志记录器名称
     */
    String getName();

    /**
     * 获取日志级别
     *
     * @return 日志级别
     */
    SqlLogger.Level getLevel();

    /**
     * 判断 Trace 日志级别是否开启
     *
     * @return 开启则返回{@code true} 否则返回{@code false}
     */
    boolean isTraceEnabled();

    /**
     * trace 级别日志输出
     *
     * @param msg 消息
     */
    void trace(String msg);

    /**
     * trace 级别日志输出
     *
     * @param format    消息格式
     * @param arguments 消息参数
     */
    void trace(String format, Object... arguments);

    /**
     * trace 级别日志输出
     *
     * @param msg 消息
     * @param t   异常
     */
    void trace(String msg, Throwable t);

    /**
     * 判断 Debug 日志级别是否开启
     *
     * @return 开启则返回{@code true} 否则返回{@code false}
     */
    boolean isDebugEnabled();

    /**
     * debug 级别日志输出
     *
     * @param msg 消息
     */
    void debug(String msg);

    /**
     * debug 级别日志输出
     *
     * @param format    消息格式
     * @param arguments 消息参数
     */
    void debug(String format, Object... arguments);

    /**
     * debug 级别日志输出
     *
     * @param msg 消息
     * @param t   异常
     */
    void debug(String msg, Throwable t);

    /**
     * 判断 Info 日志级别是否开启
     *
     * @return 开启则返回{@code true} 否则返回{@code false}
     */
    boolean isInfoEnabled();

    /**
     * info 级别日志输出
     *
     * @param msg 消息
     */
    void info(String msg);

    /**
     * info 级别日志输出
     *
     * @param format    消息格式
     * @param arguments 消息参数
     */
    void info(String format, Object... arguments);

    /**
     * info 级别日志输出
     *
     * @param msg 消息
     * @param t   异常
     */
    void info(String msg, Throwable t);

    /**
     * 判断 Warn 日志级别是否开启
     *
     * @return 开启则返回{@code true} 否则返回{@code false}
     */
    boolean isWarnEnabled();

    /**
     * warn 级别日志输出
     *
     * @param msg 消息
     */
    void warn(String msg);

    /**
     * warn 级别日志输出
     *
     * @param format    消息格式
     * @param arguments 消息参数
     */
    void warn(String format, Object... arguments);

    /**
     * warn 级别日志输出
     *
     * @param msg 消息
     * @param t   异常
     */
    void warn(String msg, Throwable t);

    /**
     * 判断 Error 日志级别是否开启
     *
     * @return 开启则返回{@code true} 否则返回{@code false}
     */
    boolean isErrorEnabled();

    /**
     * error 级别日志输出
     *
     * @param msg 消息
     */
    void error(String msg);

    /**
     * error 级别日志输出
     *
     * @param format    消息格式
     * @param arguments 消息参数
     */
    void error(String format, Object... arguments);

    /**
     * error 级别日志输出
     *
     * @param msg 消息
     * @param t   异常
     */
    void error(String msg, Throwable t);

    /**
     * 日志级别
     */
    enum Level {
        /**
         * TRACE
         */
        TRACE,
        /**
         * DEBUG
         */
        DEBUG,
        /**
         * INFO
         */
        INFO,
        /**
         * WARN
         */
        WARN,
        /**
         * ERROR
         */
        ERROR
    }

}
