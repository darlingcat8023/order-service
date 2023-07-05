package com.star.enterprise.order.http.charge.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/24
 */
public record ChargeStatusRequest(

        @NotBlank(message = "state not valid")
        String state,

        @NotNull(message = "ids not valid")
        @Size(min = 1, message = "ids not has min size of 1")
        List<String> idPairs

) {}
