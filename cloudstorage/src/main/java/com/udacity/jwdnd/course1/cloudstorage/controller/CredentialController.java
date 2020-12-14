package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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
    private EncryptionService encryptionService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, NoteService noteService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.noteService = noteService;
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

        getCredentialDetails(authentication, model);

        return "home";
    }

    @GetMapping("/credentials/delete/{credentialid}")
    public String deleteCredential(@PathVariable("credentialid") Integer credentialId, Authentication authentication, CredentialForm credentialForm, NoteForm noteForm, Model model) {
        this.credentialService.deleteCredential(credentialId);
        getCredentialDetails(authentication, model);

        return "home";
    }

    private void getCredentialDetails(Authentication authentication, Model model) {
        model.addAttribute("credentials", this.credentialService.getAllCredentials());
        model.addAttribute("notes", this.noteService.getNotes());
        model.addAttribute("encryption", this.encryptionService);
        model.addAttribute("currentid", this.userService.getUserId(authentication.getName()));
    }

}
