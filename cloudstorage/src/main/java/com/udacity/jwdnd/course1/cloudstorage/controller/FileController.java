package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class FileController {
    private CredentialService credentialService;
    private ErrorService errorService;
    private NoteService noteService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private UserService userService;
    private ErrorController errorController;

    public FileController(CredentialService credentialService, ErrorService errorService, NoteService noteService,
                          FileService fileService, EncryptionService encryptionService, UserService userService,
                          ErrorController errorController){
        this.credentialService = credentialService;
        this.errorService = errorService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
        this.errorController = errorController;

    }

    @PostMapping("/files")
    public String addFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, ErrorService errorService, CredentialForm credentialForm, NoteForm noteForm, FileForm fileForm, Model model) {
        // Get the current logged in user details
        this.fileService.trackLoggedInUserId(authentication.getName());

        //error check filename
        String filename = StringUtils.getFilename(file.getOriginalFilename());
        if (filename.trim().equals("")){
            return this.errorController.error("Cannot upload an empty file", model);
        }
        else if (this.fileService.compareFilename(filename)){
            return this.errorController.error("A file with the same name exists in the database", model);
        }

        fileForm.setFilename(filename);
        fileForm.setFileType(file.getContentType());
        fileForm.setFileSize("" + file.getSize());
        try{
            fileForm.setFileData(file.getBytes());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        this.fileService.uploadFile(fileForm);

        HomeController.updateHomeView(authentication, model, this.credentialService, this.noteService, this.fileService,
                this.encryptionService, this.userService);
        return this.errorController.error("", model);
    }


    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String filename, Authentication authentication,
                                                          FileForm fileForm, Model model){
        this.fileService.trackLoggedInUserId(authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(this.fileService.getFile(filename).getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileService.getFile(filename).getFilename() + "\"")
                .body(new ByteArrayResource(fileService.getFile(filename).getFileData()));
    }

    @GetMapping("/files/delete/{fileid}")
    public String deleteFile(@PathVariable("fileid") Integer fileId, Authentication authentication,
                             CredentialForm credentialForm, NoteForm noteForm, FileForm fileForm, Model model){
        this.fileService.deleteFile(fileId);
        for (File file : this.fileService.getAllFiles()){
            if (fileId.equals(file.getFileId())){
                this.errorController.error("File was not deleted", model);
            }
        }
        HomeController.updateHomeView(authentication, model, this.credentialService, this.noteService, this.fileService,
                this.encryptionService, this.userService);

        return this.errorController.error("", model);
    }

}
