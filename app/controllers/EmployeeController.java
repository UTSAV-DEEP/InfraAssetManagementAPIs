package controllers;

import common.dto.LoginRq;
import common.dto.SignupRq;
import common.util.Validator;
import exceptions.ApplicationException;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import services.EmployeeService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class EmployeeController extends Controller{

    @Inject
    private Validator validator;

    @Inject
    private EmployeeService employeeService;

    public CompletionStage<Result> signup()throws ApplicationException{
        return CompletableFuture.completedFuture(Results.created(
                employeeService.singup(validator.getRequest(SignupRq.class))));
    }

    public CompletionStage<Result> login() throws ApplicationException {
        return CompletableFuture.completedFuture(Results.ok(
                employeeService.login(validator.getRequest(LoginRq.class))));
    }


    public CompletionStage<Result> logout() throws ApplicationException {
        employeeService.logout();
        return CompletableFuture.completedFuture(Results.ok());
    }

}
