package com.udacity.jwdnd.course1.cloudstorage.model;

public class FileForm {
    private Integer fileId;
    private String filename;
    private String contentType;
    private String fileSize;
    private byte[] fileData;

    public Integer getFileId() {
        return fileId;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileType(String contentType) {
        this.contentType = contentType;
    }
}
