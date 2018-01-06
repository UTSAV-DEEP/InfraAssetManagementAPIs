package common.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import common.dto.AddManufacturerRq;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "manufacturing_companies")
public class ManufacturingCompany extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String registeredName;

    private String contactPerson;

    private String contactEmail;

    private String contactNumber;

    private String licenceNumber;

    @CreatedTimestamp
    private Timestamp createdAt;

    @UpdatedTimestamp
    private Timestamp updatedAt;

    public ManufacturingCompany() {
    }

    public ManufacturingCompany(AddManufacturerRq manufacturerRq) {
        this.registeredName = manufacturerRq.getRegisteredName();
        this.contactPerson = manufacturerRq.getContactPerson();
        this.contactEmail = manufacturerRq.getContactEmail();
        this.contactNumber = manufacturerRq.getContactNumber();
        this.licenceNumber = manufacturerRq.getLicenceNumber();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegisteredName() {
        return registeredName;
    }

    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
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
        ManufacturingCompany that = (ManufacturingCompany) o;
        return id == that.id &&
                Objects.equals(registeredName, that.registeredName) &&
                Objects.equals(contactPerson, that.contactPerson) &&
                Objects.equals(contactEmail, that.contactEmail) &&
                Objects.equals(contactNumber, that.contactNumber) &&
                Objects.equals(licenceNumber, that.licenceNumber) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, registeredName, contactPerson, contactEmail, contactNumber, licenceNumber, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "ManufacturingCompany{" +
                "id=" + id +
                ", registeredName='" + registeredName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static final Find<Long, ManufacturingCompany> find = new Find<Long, ManufacturingCompany>() {
    };
}
