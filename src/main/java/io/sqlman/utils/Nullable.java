package io.sqlman.utils;

/**
 * 可为空的对象
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 11:47
 */
public class Nullable<T> {
    private final T value;

    Nullable(T value) {
        this.value = value;
    }

    public static <T> Nullable<T> of(T value) {
        if (value == null) {
            throw new NullPointerException("value must not be null");
        }
        return new Nullable<>(value);
    }

    public static <T> Nullable<T> ofNullable(T value) {
        return new Nullable<>(value);
    }

    public T value() {
        return value;
    }

    public boolean isNull() {
        return value == null;
    }
}
