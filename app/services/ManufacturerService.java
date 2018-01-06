package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.dto.AddManufacturerRq;
import common.models.ManufacturingCompany;
import play.libs.Json;

import java.util.concurrent.CompletableFuture;

public class ManufacturerService {

    public CompletableFuture<ObjectNode> addManufacturer(AddManufacturerRq request) {
        ManufacturingCompany manufacturingCompany = new ManufacturingCompany(request);
        manufacturingCompany.save();
        ObjectNode response = Json.newObject();
        response.put("id",manufacturingCompany.getId());
        return CompletableFuture.completedFuture(response);
    }
}
