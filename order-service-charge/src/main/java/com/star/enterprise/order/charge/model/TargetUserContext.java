package com.star.enterprise.order.charge.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Data
@Accessors(chain = true)
public class TargetUserContext {

    private String userName;

    private String userMobile;

    private String userNumber;

    private String campusName;

    /**
     * 一级渠道
     */
    private String channelId;

    private String channelName;

    /**
     * 二级渠道
     */
    private String subChannelId;

    private String subChannelName;

}
