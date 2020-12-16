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
public class NoteController {
    private CredentialService credentialService;
    private NoteService noteService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private UserService userService;
    private ErrorController errorController;

    public NoteController(CredentialService credentialService, NoteService noteService, FileService fileService,
                          EncryptionService encryptionService, UserService userService, ErrorController errorController) {
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
        this.errorController = errorController;
    }

    @PostMapping("/notes")
    public String addUpdateNotes(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm,
                                 FileForm fileForm, Model model) {

        if(this.noteService.doesNoteExist(noteForm)) {
            this.noteService.updateNote(noteForm);
        } else {
            this.noteService.trackLoggedInUserId(authentication.getName());
            this.noteService.createNote(noteForm);
        }

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService,
                this.fileService, this.encryptionService, this.userService);
        if (this.noteService.compareTitle(noteForm.getNoteTitle())){
            return this.errorController.error("", model);
        } else{
            return this.errorController.error("There was an error uploading the note", model);
        }
    }

    @GetMapping("/notes/delete/{noteid}")
    public String deleteNotes(@PathVariable("noteid") Integer noteId, Authentication authentication, NoteForm noteForm,
                              CredentialForm credentialForm, FileForm fileForm, Model model) {
        this.noteService.deleteNote(noteId);
        for (Note note : this.noteService.getNotes()){
            if (note.getNoteId().equals(noteId)){
                return this.errorController.error("The note was not deleted", model);
            }
        }

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService,
                this.fileService, this.encryptionService, this.userService);

        return this.errorController.error("", model);
    }
}
