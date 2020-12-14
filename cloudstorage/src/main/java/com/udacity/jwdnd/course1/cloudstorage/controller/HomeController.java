package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private CredentialService credentialService;
    private NoteService noteService;
    private EncryptionService encryptionService;
    private UserService userService;

    public HomeController(CredentialService credentialService, NoteService noteService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String home(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        this.noteService.trackLoggedInUserId(username);
        this.credentialService.trackLoggedInUserId(username);

        getHomeDetails(authentication, model, this.credentialService, this.noteService, this.encryptionService, this.userService);

        return "home";
    }

    static void getHomeDetails(Authentication authentication, Model model, CredentialService credentialService, NoteService noteService, EncryptionService encryptionService, UserService userService) {
        model.addAttribute("credentials", credentialService.getAllCredentials());
        model.addAttribute("notes", noteService.getNotes());
        model.addAttribute("encryption", encryptionService);
        model.addAttribute("currentid", userService.getUserId(authentication.getName()));
    }
}
