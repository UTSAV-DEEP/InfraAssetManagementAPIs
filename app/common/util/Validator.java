package common.util;

import com.google.inject.Inject;
import common.constants.DEPARTMENT;
import common.models.Employee;
import exceptions.ApplicationException;
import org.apache.http.HttpStatus;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;

public class Validator {

    private static final Logger.ALogger LOG = Logger.of(Validator.class);

    @Inject
    private FormFactory formFactory;

    public <T> T getRequest(Class<T> clazz) throws ApplicationException {
        LOG.info("Reqeust received for {} is {}", clazz.getName(), Http.Context.current().request().body().toString());
        Form<T> form = formFactory.form(clazz).bindFromRequest();
        if (form.hasErrors()) {
            throw new ApplicationException(HttpStatus.SC_BAD_REQUEST, form.errorsAsJson().toString());
        }
        return form.get();
    }


    public Employee getLoggedInUser() throws ApplicationException{
        String sessionId = Http.Context.current().request().getHeader("X-SESSIONID");
        Http.Session session = Http.Context.current().session();
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "You are not logged in");
        }
        Employee user = Employee.find.byId(Long.valueOf(userIdStr));
        if(null == user){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "You are not logged in");
        }
        return user;
    }

    public void checkIsAdmin(Employee employee) throws ApplicationException {
        if(!DEPARTMENT.ADMIN.equals(employee.getDepartment())){
            throw new ApplicationException(HttpStatus.SC_FORBIDDEN, "Only Admin can perform this action");
        }
    }

}
