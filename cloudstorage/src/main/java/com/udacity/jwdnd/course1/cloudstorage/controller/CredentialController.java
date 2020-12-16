package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {
    private CredentialService credentialService;
    private ErrorService errorService;
    private NoteService noteService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private UserService userService;
    private ErrorController errorController;

    public CredentialController(CredentialService credentialService, ErrorService errorService, NoteService noteService,
                                FileService fileService, EncryptionService encryptionService, UserService userService,
                                ErrorController errorController) {
        this.credentialService = credentialService;
        this.errorService = errorService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
        this.errorController = errorController;
    }

    @PostMapping("/credentials")
    public String addUpdateCredential(Authentication authentication, CredentialForm credentialForm,
                                      NoteForm noteForm, FileForm fileForm, Model model) {

        // Get the current logged in user details
        this.credentialService.trackLoggedInUserId(authentication.getName());

        if(this.credentialService.doesCredentialExist(credentialForm)) {
            this.credentialService.updateCredential(credentialForm);
        } else {
            this.credentialService.createCredential(credentialForm);
        }
        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService,
                this.fileService, this.encryptionService, this.userService);


        if (this.credentialService.compareUrl(credentialForm.getUrl())){
            return this.errorController.error("", model);
        } else{
            return this.errorController.error("There was an error uploading the credential", model);
        }
    }

    @GetMapping("/credentials/delete/{credentialid}")
    public String deleteCredential(@PathVariable("credentialid") Integer credentialId, Authentication authentication, CredentialForm credentialForm, NoteForm noteForm, FileForm fileForm, Model model) {
        this.credentialService.deleteCredential(credentialId);
        for (Credential credential : this.credentialService.getAllCredentials()){
            if (credential.getCredentialId().equals(credentialId)){
                errorController.error("Credential was not deleted", model);
            }
        }

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);

        return errorController.error("", model);
    }

}
