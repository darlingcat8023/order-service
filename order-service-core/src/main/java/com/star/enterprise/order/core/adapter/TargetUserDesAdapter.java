package com.star.enterprise.order.core.adapter;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.charge.model.TargetUserContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2022/12/20
 */
@Data
@NoArgsConstructor
public class TargetUserDesAdapter implements TargetUser {

    private String targetId;

    private String campus;

    private String userStatus;

    private TargetUserContext context;

    public TargetUserDesAdapter(TargetUser target) {
        this.targetId = target.targetId();
        this.campus = target.campus();
        this.userStatus = target.executorStatus();
        this.context = Objects.requireNonNullElseGet(target.context(), TargetUserContext::new);
    }

    @Override
    public String targetId() {
        return this.targetId;
    }

    @Override
    public String executorStatus() {
        return this.userStatus;
    }

    @Override
    public String campus() {
        return this.campus;
    }

    @Override
    public TargetUserContext context() {
        return this.context;
    }

}
