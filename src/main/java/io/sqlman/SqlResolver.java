package io.sqlman;

import java.util.Comparator;
import java.util.Enumeration;

/**
 * SQL脚本解析器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:38
 */
public interface SqlResolver<T> extends Comparator<T> {

    /**
     * 验证
     *
     * @param source 源对象
     * @return 如果是合法的SQL脚本则返回{@code true} 否则返回{@code false}
     */
    boolean validate(T source);

    /**
     * 解析SQL脚本
     *
     * @param source 脚本来源
     * @return SQL脚本
     * @throws Exception 解析异常
     */
    Enumeration<SqlScript> resolve(T source) throws Exception;

}
