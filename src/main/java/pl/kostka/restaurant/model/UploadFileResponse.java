package pl.kostka.restaurant.model;

public class UploadFileResponse {

    private String fileName;
    private String downloadUri;
    private String constentType;
    private Long size;

    public UploadFileResponse(String fileName, String downloadUri, String constentType, Long size) {
        this.fileName = fileName;
        this.downloadUri = downloadUri;
        this.constentType = constentType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getConstentType() {
        return constentType;
    }

    public void setConstentType(String constentType) {
        this.constentType = constentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
