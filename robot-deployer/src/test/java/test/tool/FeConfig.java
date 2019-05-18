package test.tool;

public class FeConfig {

    private String folderName;
    private String jdbcUrl;

    private String mainTemp;
    private String formTemp;
    private String detailsTemp;


    public String getMainTemp() {
        return mainTemp;
    }

    public void setMainTemp(String mainTemp) {
        this.mainTemp = mainTemp;
    }

    public String getFormTemp() {
        return formTemp;
    }

    public void setFormTemp(String formTemp) {
        this.formTemp = formTemp;
    }

    public String getDetailsTemp() {
        return detailsTemp;
    }

    public void setDetailsTemp(String detailsTemp) {
        this.detailsTemp = detailsTemp;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}