package common.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.constants.ASSET_TYPE;
import play.libs.Json;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "assets")
public class Asset extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private ASSET_TYPE assetType;

    private double price;

    private String serialNumber;

    private String modelNumber;

    private Timestamp warrantyExpiresAt;

    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private ManufacturingCompany manufacturer;

    @CreatedTimestamp
    private Timestamp createdAt;

    @UpdatedTimestamp
    private Timestamp updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ASSET_TYPE getAssetType() {
        return assetType;
    }

    public void setAssetType(ASSET_TYPE assetType) {
        this.assetType = assetType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Timestamp getWarrantyExpiresAt() {
        return warrantyExpiresAt;
    }

    public void setWarrantyExpiresAt(Timestamp warrantyExpiresAt) {
        this.warrantyExpiresAt = warrantyExpiresAt;
    }

    public ManufacturingCompany getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturingCompany manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return id == asset.id &&
                Double.compare(asset.price, price) == 0 &&
                isAvailable == asset.isAvailable &&
                assetType == asset.assetType &&
                Objects.equals(serialNumber, asset.serialNumber) &&
                Objects.equals(modelNumber, asset.modelNumber) &&
                Objects.equals(warrantyExpiresAt, asset.warrantyExpiresAt) &&
                Objects.equals(manufacturer, asset.manufacturer) &&
                Objects.equals(createdAt, asset.createdAt) &&
                Objects.equals(updatedAt, asset.updatedAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, assetType, price, serialNumber, modelNumber, warrantyExpiresAt, isAvailable, manufacturer, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", assetType=" + assetType +
                ", price=" + price +
                ", serialNumber='" + serialNumber + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", warrantyExpiresAt=" + warrantyExpiresAt +
                ", isAvailable=" + isAvailable +
                ", manufacturer=" + manufacturer +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public ObjectNode toObjectNode() {
        ObjectNode node = Json.newObject();
        node.put("id",id);
        node.put("assetType", assetType.name());
        node.put("serialNumber", serialNumber);
        node.put("modelNumber", modelNumber);
        node.put("warrantyExpiresAt", warrantyExpiresAt.toString());
        node.put("manufacturer", manufacturer.getRegisteredName());
        node.put("price", price);
        return node;
    }

    public static final Find<Long, Asset> find = new Find<Long, Asset>() {};
}
