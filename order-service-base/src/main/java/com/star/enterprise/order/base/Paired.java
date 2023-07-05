package com.star.enterprise.order.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaowenrou
 * @date 2022/10/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paired<F, S> {

    private F first;

    private S second;

    public static <F, S> Paired<F, S> of(F f, S s) {
        return new Paired<>(f, s);
    }

}
