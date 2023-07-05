package com.star.enterprise.order.http.charge.request;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaowenrou
 * @date 2022/9/15
 */
public record BusinessSpec(

        @NotBlank(message = "spec id not valid")
        String specId,

        String specName,

        String specValue

) {}
