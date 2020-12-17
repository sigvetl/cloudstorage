package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private UserService userService;
    private EncryptionService encryptionService;
    private Integer userId;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService){
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.userId = null;
    }

    public String getEncodedKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;
    }

    public String encryptedPassword(String password, String encodedKey){
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        return encryptedPassword;
    }

    public void createCredential(CredentialForm credentialForm){
        String encodedKey = getEncodedKey();
        String encryptedPassword = encryptedPassword(credentialForm.getPassword(), encodedKey);

        Credential credential = new Credential();
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setUserId(this.userId);

        this.credentialMapper.insertCredential(credential);
    }

    public void updateCredential(CredentialForm credentialForm){
        String encodedKey = getEncodedKey();
        String encryptedPassword = encryptedPassword(credentialForm.getPassword(), encodedKey);

        Credential credential = new Credential();
        credential.setCredentialId(credentialForm.getCredentialId());
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        this.credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(Integer credentialId){
        this.credentialMapper.deleteCredential(credentialId);
    }

    public boolean doesCredentialExist(CredentialForm credentialForm) {
        Credential credential = new Credential();
        credential.setCredentialId(credentialForm.getCredentialId());
        Integer idReturned = this.credentialMapper.getCredentialId(credential);

        if (idReturned != null){
            return true;
        } else{
            return false;
        }
    }

    public boolean compareUrl(String url){
        for (Credential credential : this.getAllCredentials()){
            if (credential.getUrl().equals(url)){
                return true;
            }
        }
        return false;
    }

    public List<Credential> getAllCredentials(){
        return credentialMapper.getAllCredentials(this.userId);
    }

    public void trackLoggedInUserId(String username){
        this.userId = userService.getUser(username).getUserId();
    }
}
