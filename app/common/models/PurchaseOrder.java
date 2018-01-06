package common.models;


import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.constants.PURCHASE_STATUS;
import play.libs.Json;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "purchased_by_id")
    private Employee purchasedBy;

    private Timestamp requestedAt;

    private Timestamp expectedDelivery;

    private Timestamp deliveredAt;

    private PURCHASE_STATUS purchaseStatus;

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

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Timestamp getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Timestamp requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Timestamp getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(Timestamp expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public Timestamp getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Timestamp deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public PURCHASE_STATUS getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(PURCHASE_STATUS purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
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

    public Employee getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(Employee purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return id == that.id &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(purchasedBy, that.purchasedBy) &&
                Objects.equals(requestedAt, that.requestedAt) &&
                Objects.equals(expectedDelivery, that.expectedDelivery) &&
                Objects.equals(deliveredAt, that.deliveredAt) &&
                purchaseStatus == that.purchaseStatus &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, asset, purchasedBy, requestedAt, expectedDelivery, deliveredAt, purchaseStatus, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "id=" + id +
                ", asset=" + asset +
                ", purchasedBy=" + purchasedBy +
                ", requestedAt=" + requestedAt +
                ", expectedDelivery=" + expectedDelivery +
                ", deliveredAt=" + deliveredAt +
                ", purchaseStatus=" + purchaseStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public ObjectNode toObjectNode() {
        ObjectNode node = Json.newObject();
        node.put("id",id);
        node.put("purchasedBy", purchasedBy.getEmail());
        node.put("expectedDelivery", expectedDelivery.toString());
        node.put("requestedAt", requestedAt.toString());
        node.put("deliveredAt", deliveredAt.toString());
        node.put("purchaseStatus", purchaseStatus.name());
        node.set("asset", asset.toObjectNode());
        return node;
    }

    public static final Find<Long, PurchaseOrder> find = new Find<Long, PurchaseOrder>() {
    };
}
