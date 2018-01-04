package controllers;

import common.dto.AddFollowerRq;
import common.dto.LoginRq;
import common.dto.SignupRq;
import common.util.Validator;
import exceptions.ApplicationException;
import org.apache.http.HttpStatus;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import services.UserService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private Validator validator;

    @Inject
    private UserService userService;

    public CompletionStage<Result> signup()throws ApplicationException{
        return CompletableFuture.completedFuture(Results.created(
                userService.singup(validator.getRequest(SignupRq.class))));
    }

    public CompletionStage<Result> login() throws ApplicationException {
        return CompletableFuture.completedFuture(Results.ok(
                userService.login(validator.getRequest(LoginRq.class))));
    }


    public CompletionStage<Result> logout(){
        Http.Session session = Http.Context.current().session();
        String sessionId = request().getHeader("X-SESSIONID");
        try {
            userService.logout(session, sessionId);
            return CompletableFuture.completedFuture(Results.ok());
        } catch (ApplicationException e) {
            return e.toErrorResponse();
        }
    }

    public CompletionStage<Result> followUser() throws ApplicationException {
        String sessionId = request().getHeader("X-SESSIONID");
        userService.followUser(validator.getLoggedInUser(sessionId), validator.getRequest(AddFollowerRq.class));
        return CompletableFuture.completedFuture(Results.created());
    }

    public CompletionStage<Result> getFollowers(){
        Http.Session session = Http.Context.current().session();
        String sessionId = request().getHeader("X-SESSIONID");
        try {
            Form<AddFollowerRq> form = formFactory.form(AddFollowerRq.class).bindFromRequest();
            if (form.hasErrors()) {
                return CompletableFuture.completedFuture(Results.badRequest(form.errorsAsJson()));
            }
            userService.followUser(validator.getLoggedInUser(sessionId), validator.getRequest(AddFollowerRq.class));
            return CompletableFuture.completedFuture(Results.created());
        } catch (ApplicationException e) {
            return e.toErrorResponse();
        }
    }
}
