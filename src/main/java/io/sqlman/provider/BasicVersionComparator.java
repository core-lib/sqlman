package io.sqlman.provider;

import io.sqlman.SqlResource;

import java.util.Comparator;

/**
 * SQL脚本版本对比器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 22:02
 */
public class BasicVersionComparator implements Comparator<SqlResource> {
    private final SqlNamingStrategy namingStrategy;

    public BasicVersionComparator(SqlNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    @Override
    public int compare(SqlResource a, SqlResource b) {
        return namingStrategy.compare(a.version(), b.version());
    }
}
