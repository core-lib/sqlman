package io.sqlman.utils;

/**
 * 元素供应者
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:42
 */
public interface Supplier<R, P> {

    /**
     * 供应
     *
     * @param param 元素构造参数
     * @return 元素值
     * @throws Exception 供应异常
     */
    Nullable<R> supply(P param) throws Exception;

}
