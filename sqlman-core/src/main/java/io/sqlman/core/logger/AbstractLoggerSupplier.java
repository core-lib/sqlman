package io.sqlman.core.logger;

import io.sqlman.core.SqlLogger;
import io.sqlman.core.SqlLoggerSupplier;

/**
 * 抽象的日志记录器供应商
 *
 * @author Payne 646742615@qq.com
 * 2019/5/27 11:28
 */
public abstract class AbstractLoggerSupplier implements SqlLoggerSupplier {

    @Override
    public SqlLogger supply(Class<?> clazz) {
        return supply(clazz == null ? null : clazz.getName());
    }
}
