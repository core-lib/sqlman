package io.sqlman.provider;

import io.sqlman.SqlSource;

import java.util.Comparator;

/**
 * SQL脚本版本对比器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 22:02
 */
public class BasicVersionComparator implements Comparator<SqlSource> {
    private final Comparator<String> delegate;

    public BasicVersionComparator(Comparator<String> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int compare(SqlSource a, SqlSource b) {
        return delegate.compare(a.version(), b.version());
    }
}
