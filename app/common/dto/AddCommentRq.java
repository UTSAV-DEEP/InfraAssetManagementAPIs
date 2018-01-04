package common.dto;

import play.data.validation.Constraints;

public class AddCommentRq {

    @Constraints.MinLength(1)
    @Constraints.Required
    private String commentText;

    private Long parentId;

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "AddCommentRq{" +
                "commentText='" + commentText + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
