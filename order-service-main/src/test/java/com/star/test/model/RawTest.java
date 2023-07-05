package com.star.test.model;

import org.hibernate.validator.constraints.CreditCardNumber;

/**
 * @author xiaowenrou
 * @date 2023/1/3
 */
public record RawTest(

        @CreditCardNumber
        String token

) {}
