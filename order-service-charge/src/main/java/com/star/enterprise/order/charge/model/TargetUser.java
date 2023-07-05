package com.star.enterprise.order.charge.model;

/**
 * @author xiaowenrou
 * @date 2022/9/23
 */
public interface TargetUser {

    /**
     * 学员id
     * @return
     */
    String targetId();

    /**
     * 获取学员状态
     * @return
     */
    String executorStatus();

    /**
     * 校区
     * @return
     */
    String campus();

    /**
     * 目标用户上下文
     * @return
     */
    TargetUserContext context();

}
