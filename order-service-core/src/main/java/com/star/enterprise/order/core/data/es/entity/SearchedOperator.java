package com.star.enterprise.order.core.data.es.entity;

import com.star.enterprise.order.core.model.EmployeeUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2022/11/3
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchedOperator implements EmployeeUser {

    public static final String DEFAULT_OPERATOR_ID = "0";
    public static final String DEFAULT_OPERATOR_NAME = "system";

    @Field(type = Keyword)
    private String operatorId;

    private String operatorName;

    public static SearchedOperator defaultOperator() {
        return new SearchedOperator(DEFAULT_OPERATOR_ID, DEFAULT_OPERATOR_NAME);
    }

    @Override
    public String employeeId() {
        return this.operatorId;
    }

    @Override
    public String employeeName() {
        return this.operatorName;
    }

    @Override
    public Boolean isAdmin() {
        return null;
    }
}
