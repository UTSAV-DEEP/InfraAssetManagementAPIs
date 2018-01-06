package controllers;

import common.constants.DEPARTMENT;
import common.dto.AddManufacturerRq;
import common.dto.SignupRq;
import common.models.Employee;
import common.util.Validator;
import exceptions.ApplicationException;
import org.apache.http.HttpStatus;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ManufacturerService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ManufacturerController extends Controller {

    @Inject
    private Validator validator;

    @Inject
    private ManufacturerService manufacturerService;

    public CompletionStage<Result> addManufacturer() throws ApplicationException {
        Employee employee = validator.getLoggedInUser();
        validator.checkIsAdmin(employee);
        return manufacturerService.addManufacturer(validator.getRequest(AddManufacturerRq.class))
                .thenApply(res -> Results.created(res));
    }
}
