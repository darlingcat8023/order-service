package com.star.enterprise.order.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 框架级别的http错误码为4位
 * {XX} - {XX}  前2位表示异常模块，后2位表示异常类型
 * e.g. 参数异常 1010
 * 业务的http错误码定义为6位
 * {XX} - {XX} - {XX} 前2位为项目名 中间2位为模块名 后2位为异常类型
 * e.g. 未查到数据 101010
 * @author xiaowenrou
 * @date 2022/9/16
 */
@Getter
@AllArgsConstructor
public enum RestCode {

    /**
     * 1000 - 1999区间表示HTTP请求错误
     */
    METHOD_NOT_ALLOW(1010),

    // 安全校验失败
    HTTP_SECURITY_DECLINE(1020),

    // 触发限流
    HTTP_RATE_LIMIT(1030),

    /**
     * 2000 - 2999区间表示参数错误
     */
    ARGUMENT_NOT_VALID(2010),
    ARGUMENT_TYPE_ERROR(2020),

    /**
     * 5000 - 5999 区间表示远程服务错误
     */
    REMOTE_SERVICE_FAIL(5000),

    /**
     * 9000 - 9999区间表示未能处理的异常
     */
    UNKNOWN_ERROR(9999),

    /*------------ 订单系统业务类型以10开头 -------------*/

    /*------------ core 模块 -------------*/

    // 参数错误
    CORE_ARGUMENT_NOT_VALID(101001),

    // 目标未找到
    CORE_ITEM_NOT_FOUND(101010),

    // 操作失败
    CORE_OPERATE_ERROR(101020),

    // 不允许
    CORE_OPERATE_NOT_ALLOW(101030),

    // 深分页
    CORE_DEEP_DATA(101040),

    // 远程服务失败
    CORE_REMOTE_FAIL(101050),

    // 数据版本更新
    CORE_DATA_VERSION(101060),


    /*------------ charge 模块 -------------*/

    CHARGE_ITEM_NOT_FOUND(102010),

    CHARGE_OPERATE_ERROR(102020),

    /*------------ refund 模块 -------------*/

    REFUND_ITEM_NOT_FOUND(103010),

    REFUND_ITEM_NOT_ALLOW(103020),

    REFUND_OPERATE_ERROR(103030),

    // 深分页
    REFUND_DEEP_DATA(103040),

    /*------------ transfer 模块 -------------*/

    TRANSFER_ITEM_NOT_FOUND(104010),

    TRANSFER_ITEM_NOT_ALLOW(104020),

    TRANSFER_OPERATE_ERROR(104030),

    // 深分页
    TRANSFER_DEEP_DATA(104040),

    /*------------ receipt 模块 -------------*/
    RECEIPT_ITEM_NOT_FOUND(105010),

    RECEIPT_ITEM_NOT_ALLOW(105020),
    ;

    private final int code;
}
