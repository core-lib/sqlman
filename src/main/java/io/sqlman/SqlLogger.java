package io.sqlman;

/**
 * SQL日志记录器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/17 17:59
 */
public interface SqlLogger {

    String getName();

    SqlLogger.Level getLevel();

    boolean isTraceEnabled();

    void trace(String msg);

    void trace(String format, Object... arguments);

    void trace(String msg, Throwable t);

    boolean isDebugEnabled();

    void debug(String msg);

    void debug(String format, Object... arguments);

    void debug(String msg, Throwable t);

    boolean isInfoEnabled();

    void info(String msg);

    void info(String format, Object... arguments);

    void info(String msg, Throwable t);

    boolean isWarnEnabled();

    void warn(String msg);

    void warn(String format, Object... arguments);

    void warn(String msg, Throwable t);

    boolean isErrorEnabled();

    void error(String msg);

    void error(String format, Object... arguments);

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
