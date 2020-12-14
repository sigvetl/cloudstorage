package com.udacity.jwdnd.course1.cloudstorage.model;

public class CredentialForm {
    private Integer credentialId;
    private String url;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
