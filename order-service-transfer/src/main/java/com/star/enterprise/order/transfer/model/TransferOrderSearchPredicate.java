package com.star.enterprise.order.transfer.model;

import com.star.enterprise.order.refund.model.RefundOrderSearchPredicate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferOrderSearchPredicate extends RefundOrderSearchPredicate {

}
