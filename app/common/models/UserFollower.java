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
public class UserFollower extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "followed_by")
    private User followedBy;

    @ManyToOne
    @JoinColumn(name = "user_followed")
    private User userFollowed;

    @CreatedTimestamp
    private Timestamp createdAt;

    @UpdatedTimestamp
    private Timestamp updatedAt;

    public UserFollower() {
    }

    public UserFollower(User followedBy, User userFollowed) {
        this.followedBy = followedBy;
        this.userFollowed = userFollowed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(User followedBy) {
        this.followedBy = followedBy;
    }

    public User getUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(User userFollowed) {
        this.userFollowed = userFollowed;
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
        return "UserFollower{" +
                "id=" + id +
                ", followedBy=" + followedBy +
                ", userFollowed=" + userFollowed +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static final Find<Long, UserFollower> find = new Find<Long, UserFollower>() {
    };
}
