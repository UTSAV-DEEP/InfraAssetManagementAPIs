package exceptions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.http.HttpErrorHandler;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public class CommonExceptionHandler implements HttpErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);
    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        ObjectNode response = Json.newObject();
        response.put("message",message);
        return CompletableFuture.completedFuture(Results.status(statusCode,response));
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        LOG.error("Exception Thrown :", exception);
        if(exception instanceof ApplicationException){
            return ((ApplicationException) exception).toErrorResponse();
        }
        if(exception instanceof CompletionException && exception.getCause() instanceof ApplicationException){
            return ((ApplicationException) exception.getCause()).toErrorResponse();
        }
        return new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Something went wrong").toErrorResponse();
    }
}
