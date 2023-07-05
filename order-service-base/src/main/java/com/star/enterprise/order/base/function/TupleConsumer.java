package com.star.enterprise.order.base.function;

import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2022/12/30
 */
@FunctionalInterface
public interface TupleConsumer<T, R, U> {

    /**
     * 3消费
     * @param t
     * @param r
     * @param u
     */
    void accept(T t, R r, U u);

    default TupleConsumer<T, R, U> andThen(TupleConsumer<? super T, ? super R, ? super U> after) {
        Objects.requireNonNull(after);
        return (t, r, u) -> {
            TupleConsumer.this.accept(t, r, u);
            after.accept(t, r, u);
        };
    }

    default TupleConsumer<T, R, U> compose(TupleConsumer<? super T, ? super R, ? super U> before) {
        Objects.requireNonNull(before);
        return (t, r, u) -> {
            before.accept(t, r, u);
            TupleConsumer.this.accept(t, r, u);
        };
    }

}
