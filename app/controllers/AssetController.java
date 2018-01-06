package controllers;

import common.dto.CommonExcelRq;
import common.models.Employee;
import common.util.Validator;
import exceptions.ApplicationException;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.AssetService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AssetController extends Controller {
    @Inject
    private Validator validator;

    @Inject
    private AssetService assetService;

    public CompletionStage<Result> purchaseAsset() throws ApplicationException {
        Employee employee = validator.getLoggedInUser();
        validator.checkIsAdmin(employee);
        CommonExcelRq request = validator.getRequest(CommonExcelRq.class);
        return assetService.purchaseAsset(employee, request.getFilePath()).thenApply(res -> Results.created(res));
    }

    public CompletionStage<Result> updateAssetPurchaseOrder() throws ApplicationException {
        Employee employee = validator.getLoggedInUser();
        validator.checkIsAdmin(employee);
        CommonExcelRq request = validator.getRequest(CommonExcelRq.class);
        return assetService.updateAssetPurchaseOrder(request.getFilePath()).thenApply(res -> Results.created(res));
    }

    public CompletionStage<Result> raiseAssetRequest() throws ApplicationException {
        Employee employee = validator.getLoggedInUser();
        CommonExcelRq request = validator.getRequest(CommonExcelRq.class);
        return assetService.raiseAssetRequest(employee, request.getFilePath()).thenApply(res -> Results.created(res));
    }
}
