package common.dto;

import com.poiji.annotation.ExcelCell;
import common.constants.ASSET_TYPE;

public class AssetPurchaseRq {

    @ExcelCell(0)
    private ASSET_TYPE assetType;

    @ExcelCell(1)
    private double price;

    @ExcelCell(2)
    private String serialNumber;

    @ExcelCell(3)
    private String modelNumber;

    @ExcelCell(4)
    private String manufacturerName;

    @ExcelCell(5)
    private String warrantyExpiresAt;

    @ExcelCell(6)
    private String expectedDelivery;

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

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getWarrantyExpiresAt() {
        return warrantyExpiresAt;
    }

    public void setWarrantyExpiresAt(String warrantyExpiresAt) {
        this.warrantyExpiresAt = warrantyExpiresAt;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    @Override
    public String toString() {
        return "AssetPurchaseRq{" +
                "assetType=" + assetType +
                ", price=" + price +
                ", serialNumber='" + serialNumber + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", warrantyExpiresAt='" + warrantyExpiresAt + '\'' +
                ", expectedDelivery='" + expectedDelivery + '\'' +
                '}';
    }
}
