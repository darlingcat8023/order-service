package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.charge.model.TargetUserContext;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaowenrou
 * @date 2022/9/23
 */
public record TargetRecord(

        @NotBlank(message = "user id not valid")
        String userId,

        @NotBlank(message = "campus not valid")
        String campus,

        @NotBlank(message = "user status not valid")
        String userStatus,

        TargetUserContext context


) implements TargetUser {

        public TargetRecord(String userId, String campus, String userStatus, TargetUserContext context) {
                this.userId = userId;
                this.campus = campus;
                this.userStatus = userStatus;
                this.context = new TargetUserContext();
        }

        @Override
        public String targetId() {
                return this.userId;
        }

        @Override
        public String executorStatus() {
                return this.userStatus;
        }

}
