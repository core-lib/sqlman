package io.sqlman.core.logger;

import io.sqlman.core.SqlLogger;
import io.sqlman.core.SqlLoggerSupplier;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slf4j日志记录器供应商
 *
 * @author Payne 646742615@qq.com
 * 2019/5/27 11:19
 */
public class Slf4jLoggerSupplier extends AbstractLoggerSupplier implements SqlLoggerSupplier {
    private ILoggerFactory loggerFactory;
    private SqlLogger.Level level;

    public Slf4jLoggerSupplier() {
        this(LoggerFactory.getILoggerFactory(), SqlLogger.Level.INFO);
    }

    public Slf4jLoggerSupplier(ILoggerFactory loggerFactory) {
        this(loggerFactory, SqlLogger.Level.INFO);
    }

    public Slf4jLoggerSupplier(SqlLogger.Level level) {
        this(LoggerFactory.getILoggerFactory(), level);
    }

    public Slf4jLoggerSupplier(ILoggerFactory loggerFactory, SqlLogger.Level level) {
        if (loggerFactory == null) {
            throw new IllegalArgumentException("loggerFactory must not be null");
        }
        if (level == null) {
            throw new IllegalArgumentException("level must not be null");
        }
        this.loggerFactory = loggerFactory;
        this.level = level;
    }

    @Override
    public SqlLogger supply(String name) {
        Logger logger = loggerFactory.getLogger(name);
        return new Slf4jLogger(logger, level);
    }

    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public void setLoggerFactory(ILoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    public SqlLogger.Level getLevel() {
        return level;
    }

    public void setLevel(SqlLogger.Level level) {
        this.level = level;
    }
}
