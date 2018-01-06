package common.dto;

import com.poiji.annotation.ExcelCell;
import common.constants.ASSET_TYPE;
import play.data.validation.Constraints;

public class RaiseAssetRequestRq {

    @ExcelCell(0)
    @Constraints.Required
    private ASSET_TYPE assetType;

    @ExcelCell(1)
    @Constraints.Required
    private String employeeId;

    @ExcelCell(2)
    @Constraints.Required
    private String reason;

    public ASSET_TYPE getAssetType() {
        return assetType;
    }

    public void setAssetType(ASSET_TYPE assetType) {
        this.assetType = assetType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
