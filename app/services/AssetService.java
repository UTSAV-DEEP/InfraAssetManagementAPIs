package services;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poiji.bind.Poiji;
import common.constants.EMPLOYEE_ASSET_STATUS;
import common.constants.PURCHASE_STATUS;
import common.dto.AssetPurchaseRq;
import common.dto.RaiseAssetRequestRq;
import common.dto.UpdateAssetPurchaseRq;
import common.models.*;
import common.util.CommonUtils;
import exceptions.ApplicationException;
import org.apache.http.HttpStatus;
import play.Logger;
import play.libs.Json;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AssetService {

    private static final Logger.ALogger LOG = Logger.of(AssetService.class);

    private CompletableFuture<ObjectNode> savePurchaseOrder(Employee employee, AssetPurchaseRq request, ManufacturingCompany manufacturer) {
        if(null == manufacturer){
            throw new CompletionException(new ApplicationException(
                    HttpStatus.SC_BAD_REQUEST, "Invalid Manufacturing Company"));
        }
        Asset asset = new Asset();
        asset.setAssetType(request.getAssetType());
        asset.setManufacturer(manufacturer);
        asset.setModelNumber(request.getModelNumber());
        asset.setPrice(request.getPrice());
        asset.setAvailable(false);
        asset.setWarrantyExpiresAt(CommonUtils.stringToTimestamp(request.getWarrantyExpiresAt()));
        asset.setSerialNumber(request.getSerialNumber());
        asset.save();
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setAsset(asset);
        purchaseOrder.setExpectedDelivery(CommonUtils.stringToTimestamp(request.getExpectedDelivery()));
        purchaseOrder.setPurchasedBy(employee);
        purchaseOrder.setPurchaseStatus(PURCHASE_STATUS.ORDER_PLACED);
        purchaseOrder.save();
        LOG.info("Saved purchase order: {}",purchaseOrder.toString());
        return CompletableFuture.completedFuture(purchaseOrder.toObjectNode());
    }

    public CompletableFuture<ObjectNode> purchaseAsset(Employee employee, String filePath) {
        List<AssetPurchaseRq> purchaseRequests = Poiji.fromExcel(new File(filePath), AssetPurchaseRq.class);
        Map<String, ManufacturingCompany> cachedManufacturer = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ArrayNode purchaseOrdersJson = Json.newArray();
        ArrayNode errors = Json.newArray();
        purchaseRequests.forEach(request -> {
            CompletableFuture<Void> future = new CompletableFuture<>();
            futures.add(future);
            ManufacturingCompany manufacturer = cachedManufacturer.get(request.getManufacturerName());
            if(null == manufacturer) {
                manufacturer = ManufacturingCompany.find.where().eq("registered_name", request.getManufacturerName()).findUnique();
                cachedManufacturer.put(request.getManufacturerName(), manufacturer);
            }
            savePurchaseOrder(employee, request, manufacturer).thenApply(savedOrder -> {
                purchaseOrdersJson.add(savedOrder);
                return future.complete(null);
            }).exceptionally(e-> {
                LOG.error("Unable to create purchase order for request: {}", request, e);
                if(null != e.getCause() && e.getCause() instanceof ApplicationException){
                    errors.add(e.getMessage());
                }
                else{
                    errors.add("Something went wrong");
                }
                return future.complete(null);
            });
        });
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(dummyResp -> {
            ObjectNode response = Json.newObject();
            response.set("placedOrders", purchaseOrdersJson);
            if(errors.size() > 0){
                response.set("errors", errors);
            }
            return response;
        });
    }


    private CompletableFuture<ObjectNode> saveUpdatedPurchaseOrder(UpdateAssetPurchaseRq request, PurchaseOrder purchaseOrder) {
        if(null == purchaseOrder){
            throw new CompletionException(new ApplicationException(HttpStatus.SC_BAD_REQUEST, "Invalid Purchase Order Id"));
        }
        Asset asset = purchaseOrder.getAsset();
        EmployeeAsset employeeAsset = EmployeeAsset.find.where().eq("assets.id", asset.getId()).findUnique();
        if(PURCHASE_STATUS.DELIVERED.equals(request.getPurchaseStatus())){
            if(null == employeeAsset) {
                asset.setAvailable(true);
                asset.save();
            }
            else {
                employeeAsset.setIssuedAt(new Timestamp(System.currentTimeMillis()));
                employeeAsset.setStatus(EMPLOYEE_ASSET_STATUS.FULFILLED);
                employeeAsset.save();
            }
            purchaseOrder.setAsset(asset);
            purchaseOrder.setDeliveredAt(new Timestamp(System.currentTimeMillis()));
        }
        else if(PURCHASE_STATUS.CANCELLED.equals(request.getPurchaseStatus()) && null != employeeAsset){
            employeeAsset.setStatus(EMPLOYEE_ASSET_STATUS.CANCELLED);
            employeeAsset.save();
        }
        purchaseOrder.setExpectedDelivery(CommonUtils.stringToTimestamp(request.getExpectedDelivery()));
        purchaseOrder.setPurchaseStatus(request.getPurchaseStatus());
        purchaseOrder.save();
        LOG.info("Saved purchase order: {}",purchaseOrder.toString());
        return CompletableFuture.completedFuture(purchaseOrder.toObjectNode());
    }

    public CompletableFuture<ObjectNode> updateAssetPurchaseOrder(String filePath) {
        List<UpdateAssetPurchaseRq> updatePurchaseRqs = Poiji.fromExcel(new File(filePath), UpdateAssetPurchaseRq.class);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ArrayNode purchaseOrdersJson = Json.newArray();
        ArrayNode errors = Json.newArray();
        updatePurchaseRqs.forEach(request -> {
            PurchaseOrder purchaseOrder = PurchaseOrder.find.byId(request.getPurchaseOrderId());
            CompletableFuture<Void> future = new CompletableFuture<>();
            futures.add(future);
            saveUpdatedPurchaseOrder(request, purchaseOrder).thenApply(savedOrder -> {
                purchaseOrdersJson.add(savedOrder);
                return future.complete(null);
            }).exceptionally(e-> {
                LOG.error("Unable to update purchase order for request: {}", request, e);
                if(null != e.getCause() && e.getCause() instanceof ApplicationException){
                    errors.add(e.getMessage());
                }
                else{
                    errors.add("Something went wrong");
                }
                return future.complete(null);
            });
        });
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(dummyResp -> {
            ObjectNode response = Json.newObject();
            response.set("updatedOrders", purchaseOrdersJson);
            if(errors.size() > 0){
                response.set("errors", errors);
            }
            return response;
        });
    }




    private CompletableFuture<ObjectNode> saveEmployeeAssets(Employee employee, RaiseAssetRequestRq request) {
        EmployeeAsset employeeAsset = new EmployeeAsset();
        Transaction t = Ebean.beginTransaction();
        try {
            Query<Asset> query = Ebean.createQuery(Asset.class);
            query.where().eq("accet_type", request.getAssetType().name())
                    .eq("is_available",true)
                    .gt("warranty_expires_at",new Timestamp(System.currentTimeMillis()));
            query.setForUpdate(true);
            Asset assetToAssign = query.setMaxRows(1).findUnique();
            if(null != assetToAssign){
                assetToAssign.setAvailable(false);
                assetToAssign.save();
                employeeAsset.setAsset(assetToAssign);
                employeeAsset.setEmployee(employee);
                employeeAsset.setRequestedAt(new Timestamp(System.currentTimeMillis()));
                employeeAsset.setIssuedAt(new Timestamp(System.currentTimeMillis()));
                employeeAsset.setReason(request.getReason());
                employeeAsset.setStatus(EMPLOYEE_ASSET_STATUS.FULFILLED);
                employeeAsset.save();
            }
            else {
                Query<PurchaseOrder> purchaseOrderQuery = Ebean.createQuery(PurchaseOrder.class);
                purchaseOrderQuery.where()
                        .ne("purchase_status", PURCHASE_STATUS.CANCELLED)
                        .eq("assets.asset_type", request.getAssetType())
                        .orderBy("expected_delivery");
                purchaseOrderQuery.setForUpdate(true);
                PurchaseOrder purchaseOrder = purchaseOrderQuery.setMaxRows(1).findUnique();
                if(null == purchaseOrder){
                    throw new ApplicationException(HttpStatus.SC_NOT_FOUND,
                            String.format("Not enough instance of %s available, please create purchase order", request.getAssetType()));
                }
                employeeAsset.setReason(request.getReason());
                employeeAsset.setEmployee(employee);
                employeeAsset.setRequestedAt(new Timestamp(System.currentTimeMillis()));
                employeeAsset.setExpectedIssueDate(purchaseOrder.getExpectedDelivery());
                employeeAsset.setAsset(purchaseOrder.getAsset());
                employeeAsset.setStatus(EMPLOYEE_ASSET_STATUS.IN_PROGRESS);
                employeeAsset.save();
            }
            t.commit();
        } catch (Exception e) {
            LOG.error("Error while assigning asset to the employee for request: {}", request.toString(), e);
            t.rollback();
            throw new CompletionException(e);
        } finally {
            t.end();
        }
        return CompletableFuture.completedFuture(employeeAsset.toObjectNode());
    }


    public CompletableFuture<ObjectNode> raiseAssetRequest(Employee employee, String filePath) {
        List<RaiseAssetRequestRq> raiseAssetRequestRqs = Poiji.fromExcel(new File(filePath), RaiseAssetRequestRq.class);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ArrayNode requestedAssets = Json.newArray();
        ArrayNode errors = Json.newArray();
        raiseAssetRequestRqs.forEach(request -> {
            CompletableFuture<Void> future = new CompletableFuture<>();
            futures.add(future);
            saveEmployeeAssets(employee, request).thenApply(savedOrder -> {
                requestedAssets.add(savedOrder);
                return future.complete(null);
            }).exceptionally(e-> {
                LOG.error("Unable to raise assetRequest for request: {}", request, e);
                if(null != e.getCause() && e.getCause() instanceof ApplicationException){
                    errors.add(e.getMessage());
                }
                else{
                    errors.add("Something went wrong");
                }
                return future.complete(null);
            });
        });
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(dummyResp -> {
            ObjectNode response = Json.newObject();
            response.set("requestedAssets", requestedAssets);
            if(errors.size() > 0){
                response.set("errors", errors);
            }
            return response;
        });
    }

}
