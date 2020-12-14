package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private UserService userService;
    private Integer userId;

    public CredentialService(CredentialMapper credentialMapper, UserService userService){
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.userId = null;
    }

    public void createCredential(CredentialForm credentialForm){
        Credential credential = new Credential();
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        credential.setPassword(credentialForm.getPassword());
        credential.setUserId(this.userId);

        this.credentialMapper.insertCredential(credential);
    }

    public void updateCredential(CredentialForm credentialForm){
        Credential credential = new Credential();
        credential.setCredentialId(credentialForm.getCredentialId());
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        credential.setPassword(credentialForm.getPassword());

        this.credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(Integer credentialId){
        this.credentialMapper.deleteCredential(credentialId);
    }

    public boolean doesCredentialExist(CredentialForm credentialForm) {
        Credential credential = new Credential();
        credential.setCredentialId(credentialForm.getCredentialId());
        Integer idReturned = this.credentialMapper.getCredentialById(credential);

        if (idReturned != null){
            return true;
        } else{
            return false;
        }
        /*
        for (Credential c : this.getAllCredentials()) {
            if (c.getUsername().equals(credential.getUsername())) {
                return true;
            }
        }
        return false;

         */
    }

    public List<Credential> getAllCredentials(){
        return credentialMapper.getCredentials(this.userId);
    }

    public void trackLoggedInUserId(String username){
        this.userId = userService.getUser(username).getUserId();
    }
}
