package common.dto;

import com.poiji.annotation.ExcelCell;
import common.constants.PURCHASE_STATUS;

import java.sql.Timestamp;

public class UpdateAssetPurchaseRq {

    @ExcelCell(0)
    private Long purchaseOrderId;

    @ExcelCell(1)
    private String expectedDelivery;

    @ExcelCell(2)
    private PURCHASE_STATUS purchaseStatus;

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public PURCHASE_STATUS getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(PURCHASE_STATUS purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    @Override
    public String toString() {
        return "UpdateAssetPurchaseRq{" +
                "purchaseOrderId=" + purchaseOrderId +
                ", expectedDelivery='" + expectedDelivery + '\'' +
                ", purchaseStatus=" + purchaseStatus +
                '}';
    }
}
