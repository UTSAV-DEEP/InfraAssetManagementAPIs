package common.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.util.ParagraphClassifier;
import play.libs.Json;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
public class Comment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String commentText;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedTimestamp
    private Timestamp createdAt;

    @UpdatedTimestamp
    private Timestamp updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentText='" + commentText + '\'' +
                ", parent=" + parent +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public ObjectNode toObjectNode(ParagraphClassifier classifier){
        ObjectNode response = Json.newObject();
        response.put("id", id);
        response.put("comment", commentText);
        response.set("user", user.toObjectNode());
        if(null != parent) {
            response.set("parent", parent.toObjectNode(classifier));
        }
        response.set("labels", classifier.getLabelsForComment(commentText).get("labels"));
        return response;
    }

    public static final Find<Long, Comment> find = new Find<Long, Comment>() {
    };
}
