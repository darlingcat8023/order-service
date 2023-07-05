package com.star.enterprise.order.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceUser {

    @Field(type = Keyword)
    private String userId;

    private String userName;

    @Field(type = Keyword)
    private String userRole;

}
