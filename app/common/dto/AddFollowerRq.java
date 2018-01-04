package common.dto;

import play.data.validation.Constraints;

public class AddFollowerRq {
    @Constraints.Required
    private Long toFollowUserId;

    public Long getToFollowUserId() {
        return toFollowUserId;
    }

    public void setToFollowUserId(Long toFollowUserId) {
        this.toFollowUserId = toFollowUserId;
    }
}
