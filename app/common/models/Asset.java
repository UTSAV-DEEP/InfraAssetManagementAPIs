package common.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import common.constants.ASSET_TYPE;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private long manufacturer;

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

    public long getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(long manufacturer) {
        this.manufacturer = manufacturer;
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
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", assetType=" + assetType +
                ", price=" + price +
                ", serialNumber='" + serialNumber + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", warrantyExpiresAt=" + warrantyExpiresAt +
                ", manufacturer=" + manufacturer +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static final Find<Long, Asset> find = new Find<Long, Asset>() {};
}
