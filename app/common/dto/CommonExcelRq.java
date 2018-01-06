package common.dto;

import play.data.validation.Constraints;

public class CommonExcelRq {

    @Constraints.Required
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
