package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
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
    private NoteService noteService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, NoteService noteService, FileService fileService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @PostMapping("/credentials")
    public String addUpdateCredential(Authentication authentication, CredentialForm credentialForm, NoteForm noteForm, Model model) {

        // Get the current logged in user details
        this.credentialService.trackLoggedInUserId(authentication.getName());

        if(this.credentialService.doesCredentialExist(credentialForm)) {
            this.credentialService.updateCredential(credentialForm);
        } else {
            this.credentialService.createCredential(credentialForm);
        }

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);

        return "home";
    }

    @GetMapping("/credentials/delete/{credentialid}")
    public String deleteCredential(@PathVariable("credentialid") Integer credentialId, Authentication authentication, CredentialForm credentialForm, NoteForm noteForm, FileForm fileForm, Model model) {
        this.credentialService.deleteCredential(credentialId);
        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);

        return "home";
    }

}
