package com.star.enterprise.order.base.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author xiaowenrou
 * @date 2022/12/27
 */
@FunctionalInterface
public interface TupleFunction<T, R, U, S> {

    /**
     * 3换1方法
     * @param t
     * @param r
     * @param u
     * @return
     */
    S apply(T t, R r, U u);

    default <V> TupleFunction<T, R, U, V> andThen(Function<? super S, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t, r, u) -> after.apply(TupleFunction.this.apply(t, r, u));
    }

}
