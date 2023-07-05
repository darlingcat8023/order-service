package com.star.enterprise.order.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/4/20
 */
@Data
public class PermissionQuery {

    private List<String> selectedCampus = new ArrayList<>();

}
