package com.star.enterprise.order.receipt.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class ReceiptContext {

    private Set<String> operators = new HashSet<>();

}
