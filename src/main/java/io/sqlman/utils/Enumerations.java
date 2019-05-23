package io.sqlman.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * 惰性枚举器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 13:20
 */
public final class Enumerations {

    public static <R, P> Enumeration<R> create(P param, Supplier<R, P> supplier) {
        return new Single<>(param, supplier);
    }

    public static <R, P> Enumeration<R> create(Enumeration<P> param, Supplier<Enumeration<R>, P> supplier) {
        return new Multiple<>(param, supplier);
    }

    /**
     * 单个枚举值的枚举类
     *
     * @author Payne 646742615@qq.com
     * 2019/5/22 11:41
     */
    private static class Single<R, P> implements Enumeration<R> {
        private final P param;
        private final Supplier<R, P> supplier;
        private volatile Nullable<R> element;
        private volatile boolean supplied;

        Single(P param, Supplier<R, P> supplier) {
            if (supplier == null) {
                throw new NullPointerException("supplier must not be null");
            }
            this.param = param;
            this.supplier = supplier;
        }

        @Override
        public boolean hasMoreElements() {
            if (!supplied) {
                try {
                    element = supplier.supply(param);
                    supplied = true;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            return element != null;
        }

        @Override
        public R nextElement() {
            if (hasMoreElements()) {
                R result = element.value();
                element = null;
                return result;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * 多个元素的枚举器
     *
     * @author Payne 646742615@qq.com
     * 2019/5/22 13:19
     */
    private static class Multiple<R, P> implements Enumeration<R> {
        private final Enumeration<P> param;
        private final Supplier<Enumeration<R>, P> supplier;
        private volatile Nullable<Enumeration<R>> next;

        Multiple(Enumeration<P> param, Supplier<Enumeration<R>, P> supplier) {
            if (param == null) {
                throw new NullPointerException("param must not be null");
            }
            if (supplier == null) {
                throw new NullPointerException("supplier must not be null");
            }
            this.param = param;
            this.supplier = supplier;
        }

        @Override
        public boolean hasMoreElements() {
            try {
                if (next == null || next.isNull()) {
                    if (param.hasMoreElements()) {
                        next = supplier.supply(param.nextElement());
                        return hasMoreElements();
                    }
                    return false;
                }
                return next.value().hasMoreElements();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public R nextElement() {
            if (hasMoreElements()) {
                R element = next.value().nextElement();
                next = null;
                return element;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
