package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.dto.AddFollowerRq;
import common.dto.LoginRq;
import common.dto.SignupRq;
import common.models.User;
import common.models.UserFollower;
import exceptions.ApplicationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpStatus;
import play.libs.Json;
import play.mvc.Http;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserService {

    public ObjectNode singup(SignupRq signupRq){
        User user = new User();
        user.setEmail(signupRq.getEmail());
        user.setMobile(signupRq.getMobile());
        user.setName(signupRq.getName());
        String hashedPassword = DigestUtils.sha256Hex(signupRq.getPassword());
        user.setHashedPassword(hashedPassword);
        user.save();
        ObjectNode response = Json.newObject();
        response.put("id", user.getId());
        return response;
    }

    public ObjectNode login(LoginRq loginRq) throws ApplicationException {
        Http.Session session = Http.Context.current().session();
        String hashedPassword = DigestUtils.sha256Hex(loginRq.getPassword());
        User user = User.find.where()
                .eq("email", loginRq.getEmail())
                .eq("hashed_password", hashedPassword).findUnique();
        if(null == user){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "Invalid email or password");
        }
        String uuid = UUID.randomUUID().toString();
        session.put(uuid, String.valueOf(user.getId()));
        ObjectNode response = Json.newObject();
        response.put("accessToken", uuid);
        return response;
    }

    public void logout(Http.Session session, String sessionId) throws ApplicationException {
        String userIdStr = session.get(sessionId);
        if(null == userIdStr){
            throw new ApplicationException(HttpStatus.SC_UNAUTHORIZED, "You are not logged in");
        }
        session.remove(sessionId);
    }

    public void followUser(User user, AddFollowerRq request) throws ApplicationException {
        User userFollowed = User.find.byId(request.getToFollowUserId());
        if(null == userFollowed){
            throw new ApplicationException(HttpStatus.SC_NOT_FOUND, "User to follow does not exist");
        }
        UserFollower userFollower = new UserFollower(user,userFollowed);
        userFollower.save();
    }
}
