package com.star.enterprise.order.core.handler.order;

/**
 * @author xiaowenrou
 * @date 2023/3/21
 */
public interface StatusTransactionAsyncHandler<E> {

    /**
     * 订单提交处理器，这个方法异步执行的，所有ThreadLocal都取不到
     * @param event
     */
    void onCurrentStatusCommitTransactionAsync(final E event);

    /**
     * 订单回滚处理器
     * @param event
     */
    default void onCurrentStatusRollbackTransactionAsync(final E event) {}

}
