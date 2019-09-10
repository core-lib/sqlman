package io.sqlman.core.logger;

import io.sqlman.core.SqlLogger;
import org.slf4j.Logger;

/**
 * Slf4j日志记录器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/27 11:20
 */
public class Slf4jLogger implements SqlLogger {
    private final Logger logger;
    private final SqlLogger.Level level;

    public Slf4jLogger(Logger logger) {
        this(logger, Level.INFO);
    }

    public Slf4jLogger(Logger logger, SqlLogger.Level level) {
        if (logger == null) {
            throw new IllegalArgumentException("logger must not be null");
        }
        if (level == null) {
            throw new IllegalArgumentException("level must not be null");
        }
        this.logger = logger;
        this.level = level;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public Level getLevel() {
        return level;
    }


    @Override
    public boolean isTraceEnabled() {
        return Level.TRACE.compareTo(level) >= 0 && logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            logger.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            logger.trace(format, arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            logger.trace(msg, t);
        }
    }


    @Override
    public boolean isDebugEnabled() {
        return Level.DEBUG.compareTo(level) >= 0 && logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        if (isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            logger.debug(format, arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            logger.debug(msg, t);
        }
    }


    @Override
    public boolean isInfoEnabled() {
        return Level.INFO.compareTo(level) >= 0 && logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        if (isInfoEnabled()) {
            logger.info(msg);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            logger.info(msg, t);
        }
    }


    @Override
    public boolean isWarnEnabled() {
        return Level.WARN.compareTo(level) >= 0 && logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        if (isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            logger.warn(format, arguments);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (isWarnEnabled()) {
            logger.warn(msg, t);
        }
    }


    @Override
    public boolean isErrorEnabled() {
        return Level.ERROR.compareTo(level) >= 0 && logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        if (isErrorEnabled()) {
            logger.error(msg);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            logger.error(format, arguments);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (isErrorEnabled()) {
            logger.error(msg, t);
        }
    }
}
