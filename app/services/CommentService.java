package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.dto.AddCommentRq;
import common.models.Comment;
import common.models.User;
import common.util.ParagraphClassifier;
import exceptions.ApplicationException;
import org.apache.http.HttpStatus;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

public class CommentService {

    public ObjectNode addComment(long userId, AddCommentRq addCommentRq){
        User user = User.find.byId(userId);
        Comment comment = new Comment();
        if(addCommentRq.getParentId()!=null){
            Comment parentComment = Comment.find.byId(addCommentRq.getParentId());
            if(null != parentComment){
                comment.setParent(parentComment);
            }
        }
        comment.setCommentText(addCommentRq.getCommentText());
        comment.setUser(user);
        comment.save();
        ObjectNode response = Json.newObject();
        response.put("id",comment.getId());
        return response;
    }

    public ObjectNode getUserComments(long userId) throws ApplicationException {
        User user = User.find.byId(userId);
        if(null == user){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "User does not exist");
        }
        List<Comment> userComments = Comment.find.where().eq("user_id", userId).findList();
        if(null == userComments){
            userComments = new ArrayList<>();
        }
        ArrayNode comments = Json.newArray();
        try {
            ParagraphClassifier classifier = ParagraphClassifier.getInstance();

            for(Comment comment: userComments){
                comments.add(comment.toObjectNode(classifier));
            }
            ObjectNode response = Json.newObject();
            response.set("comments", comments);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Error while processing your request");
        }
    }
}
