package exceptions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApplicationException extends Exception {
    private int status;

    public ApplicationException() {
    }

    public ApplicationException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CompletionStage<Result> toErrorResponse(){
        ObjectNode response = Json.newObject();
        response.put("message", super.getMessage());
        return CompletableFuture.completedFuture(Results.status(status,response));
    }
}
