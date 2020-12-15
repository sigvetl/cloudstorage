package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {
    private CredentialService credentialService;
    private NoteService noteService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private UserService userService;

    public NoteController(CredentialService credentialService, NoteService noteService, FileService fileService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public String addUpdateNotes(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, FileForm fileForm, Model model) {
        // Check if data exists in database already
        // We use this to update our data by searching
        // for our unique id.
        //String success = null;

        if(this.noteService.doesNoteExist(noteForm)) {
            this.noteService.updateNote(noteForm);
            //success = "Note " + noteForm.getNoteTitle() + " was modified successfully!";
        } else {
            this.noteService.trackLoggedInUserId(authentication.getName());
            this.noteService.createNote(noteForm);
            //success = "Note: " + noteForm.getNoteTitle() + " was created successfully!";
        }

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);
        //model.addAttribute("success", success);

        return "home";
    }

    @GetMapping("/notes/delete/{noteid}")
    public String deleteNotes(@PathVariable("noteid") Integer noteId, Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, FileForm fileForm, Model model) {
        this.noteService.deleteNote(noteId);
        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);
        //model.addAttribute("success", "Note was deleted successfully.");

        return "home";
    }
}
