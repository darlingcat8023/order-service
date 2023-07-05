package com.star.enterprise.order.core.data.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchedTarget {

    private String targetName;

    @Field(type = Keyword)
    private String targetId;

    @Field(type = Keyword)
    private String targetCampus;

    @Field(type = Keyword)
    private String targetMobile;

    @Field(type = Keyword)
    private String targetNumber;

    private String targetCampusName;

    @Field(type = Keyword)
    private String channelId;

    @Field(type = Keyword)
    private String channelName;

    @Field(type = Keyword)
    private String subChannelId;

    @Field(type = Keyword)
    private String subChannelName;

}
