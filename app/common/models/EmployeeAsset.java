package common.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.constants.EMPLOYEE_ASSET_STATUS;
import play.libs.Json;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "employee_assets")
public class EmployeeAsset extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private String reason;

    private EMPLOYEE_ASSET_STATUS status;

    private Timestamp requestedAt;

    private Timestamp expectedIssueDate;

    private Timestamp issuedAt;

    private Timestamp returnedAt;

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public Timestamp getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Timestamp issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Timestamp getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(Timestamp returnedAt) {
        this.returnedAt = returnedAt;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getExpectedIssueDate() {
        return expectedIssueDate;
    }

    public void setExpectedIssueDate(Timestamp expectedIssueDate) {
        this.expectedIssueDate = expectedIssueDate;
    }

    public EMPLOYEE_ASSET_STATUS getStatus() {
        return status;
    }

    public void setStatus(EMPLOYEE_ASSET_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAsset that = (EmployeeAsset) o;
        return id == that.id &&
                Objects.equals(employee, that.employee) &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(reason, that.reason) &&
                status == that.status &&
                Objects.equals(requestedAt, that.requestedAt) &&
                Objects.equals(expectedIssueDate, that.expectedIssueDate) &&
                Objects.equals(issuedAt, that.issuedAt) &&
                Objects.equals(returnedAt, that.returnedAt) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, employee, asset, reason, status, requestedAt, expectedIssueDate, issuedAt, returnedAt, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "EmployeeAsset{" +
                "id=" + id +
                ", employee=" + employee +
                ", asset=" + asset +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", requestedAt=" + requestedAt +
                ", expectedIssueDate=" + expectedIssueDate +
                ", issuedAt=" + issuedAt +
                ", returnedAt=" + returnedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public ObjectNode toObjectNode(){
        ObjectNode node = Json.newObject();
        node.put("id",id);
        node.put("employee", employee.getEmail());
        node.put("reason", reason);
        node.set("asset", asset.toObjectNode());
        node.put("requestedAt", requestedAt.toString());
        node.put("expectedIssueDate", expectedIssueDate.toString());
        node.put("actualIssueDate", issuedAt.toString());
        node.put("status", status.name());
        return node;
    }

    public static final Find<Long, EmployeeAsset> find = new Find<Long, EmployeeAsset>() {
    };
}
