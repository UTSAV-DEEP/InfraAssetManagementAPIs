package common.dto;

import common.constants.DEPARTMENT;
import play.data.validation.Constraints;

public class SignupRq {

    @Constraints.Required
    private String name;

    @Constraints.Email
    @Constraints.Required
    private String email;

    @Constraints.Required
    private String mobile;

    @Constraints.Required
    private String designation;

    @Constraints.Required
    private DEPARTMENT department;

    @Constraints.Required
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public DEPARTMENT getDepartment() {
        return department;
    }

    public void setDepartment(DEPARTMENT department) {
        this.department = department;
    }
}
