package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
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

    private NoteService noteService;
    private EncryptionService encryptionService;
    private UserService userService;

    public HomeController(NoteService noteService, EncryptionService encryptionService, UserService userService) {
        this.noteService = noteService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String home(Authentication authentication, NoteForm noteForm, Model model) {
        String username = authentication.getName();
        this.noteService.trackLoggedInUserId(username);

        getHomeDetails(authentication, model, this.noteService, this.encryptionService, this.userService);

        return "home";
    }

    static void getHomeDetails(Authentication authentication, Model model, NoteService noteService, EncryptionService encryptionService, UserService userService) {
        model.addAttribute("notes", noteService.getNotes());
        model.addAttribute("encryption", encryptionService);
        model.addAttribute("currentid", userService.getUserId(authentication.getName()));
    }
}
