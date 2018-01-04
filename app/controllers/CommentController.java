package controllers;

import com.google.inject.Inject;
import common.dto.AddCommentRq;
import common.models.Comment;
import exceptions.ApplicationException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Results;
import services.CommentService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class CommentController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private CommentService commentService;

    public CompletionStage<Result> addComment(){
        Session session = Http.Context.current().session();
        Form<AddCommentRq> form = formFactory.form(AddCommentRq.class).bindFromRequest();
        if (form.hasErrors()) {
            return CompletableFuture.completedFuture(Results.badRequest(form.errorsAsJson()));
        }
        String sessionId = request().getHeader("X-SESSIONID");
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            return CompletableFuture.completedFuture(Results.unauthorized());
        }
        Long userId = Long.parseLong(userIdStr);
        return CompletableFuture.completedFuture(Results.ok(commentService.addComment(userId,form.get())));
    }


    public CompletionStage<Result> getUserComments() throws ApplicationException {
        Session session = Http.Context.current().session();
        String sessionId = request().getHeader("X-SESSIONID");
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            return CompletableFuture.completedFuture(Results.unauthorized());
        }
        Long userId = Long.parseLong(userIdStr);
        List<Comment> userComments = Comment.find.where().eq("user_id",userId).findList();
        return CompletableFuture.completedFuture(Results.ok(commentService.getUserComments(userId)));
    }

    public CompletionStage<Result> getHomePageComments() throws ApplicationException {
        Session session = Http.Context.current().session();
        String sessionId = request().getHeader("X-SESSIONID");
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            return CompletableFuture.completedFuture(Results.unauthorized());
        }
        Long userId = Long.parseLong(userIdStr);
        List<Comment> userComments = Comment.find.where().eq("user_id",userId).findList();
        return CompletableFuture.completedFuture(Results.ok(commentService.getUserComments(userId)));
    }


}
