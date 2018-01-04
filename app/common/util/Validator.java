package common.util;

import com.google.inject.Inject;
import common.dto.SignupRq;
import common.models.User;
import exceptions.ApplicationException;
import org.apache.http.HttpStatus;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;

public class Validator {

    @Inject
    private FormFactory formFactory;

    public <T> T getRequest(Class<T> clazz) throws ApplicationException {

        Form<T> form = formFactory.form(clazz).bindFromRequest();
        if (form.hasErrors()) {
            throw new ApplicationException(HttpStatus.SC_BAD_REQUEST, form.errorsAsJson().toString());
        }
        return form.get();
    }

    public User getLoggedInUser(String sessionId) throws ApplicationException{
        Http.Session session = Http.Context.current().session();
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "You are not logged in");
        }
        User user = User.find.byId(Long.valueOf(userIdStr));
        if(null == user){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "You are not logged in");
        }
        return user;
    }
}
