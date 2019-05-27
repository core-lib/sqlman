package io.sqlman.logger;

import io.sqlman.SqlLogger;
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
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }
}
