package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.dto.LoginRq;
import common.dto.SignupRq;
import common.models.Employee;
import exceptions.ApplicationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpStatus;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Http;

import java.util.UUID;

public class EmployeeService {


    public ObjectNode singup(SignupRq signupRq){
        Employee employee = new Employee();
        employee.setEmail(signupRq.getEmail());
        employee.setMobile(signupRq.getMobile());
        employee.setName(signupRq.getName());
        employee.setDesignation(signupRq.getDesignation());

        String hashedPassword = DigestUtils.sha256Hex(signupRq.getPassword());
        employee.setHashedPassword(hashedPassword);
        employee.save();
        ObjectNode response = Json.newObject();
        response.put("id", employee.getId());
        return response;
    }

    public ObjectNode login(LoginRq loginRq) throws ApplicationException {
        Http.Session session = Http.Context.current().session();
        String hashedPassword = DigestUtils.sha256Hex(loginRq.getPassword());
        Employee employee = Employee.find.where()
                .eq("email", loginRq.getEmail())
                .eq("hashed_password", hashedPassword).findUnique();
        if(null == employee){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "Invalid email or password");
        }
        String uuid = UUID.randomUUID().toString();
        session.put(uuid, String.valueOf(employee.getId()));
        ObjectNode response = Json.newObject();
        response.put("accessToken", uuid);
        response.set("employeeData", employee.toObjectNode());
        return response;
    }

    public void logout() throws ApplicationException {
        String sessionId = Http.Context.current().request().getHeader("X-SESSIONID");
        Http.Session session = Http.Context.current().session();
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "You are not logged in");
        }
        session.remove(sessionId);
    }
}
