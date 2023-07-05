package com.star.enterprise.order.transfer.model;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface TransferExtendInfo {

    String remark();

    String transferReason();

    List<String> files();

}
