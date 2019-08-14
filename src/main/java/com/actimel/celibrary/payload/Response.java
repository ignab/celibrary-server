package com.actimel.celibrary.payload;

public class Response {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private String type_id;

    public Response(String fileName, String fileDownloadUri, String fileType, long size, String type_id) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.setFileType(fileType);
        this.size = size;
        this.type_id = type_id;
    }
    // Getters and Setters (Omitted for brevity)

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

}