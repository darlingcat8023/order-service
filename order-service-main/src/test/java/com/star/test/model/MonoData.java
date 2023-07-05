package com.star.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaowenrou
 * @date 2022/12/2
 */
@Data
@Accessors(chain = true)
public class MonoData {

    private String first;

    private String second;

    private String third;

}
