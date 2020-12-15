package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
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
    private NoteService noteService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private UserService userService;

    public FileController(CredentialService credentialService, NoteService noteService, FileService fileService, EncryptionService encryptionService, UserService userService){
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @PostMapping("/files")
    public String addFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, CredentialForm credentialForm, NoteForm noteForm, FileForm fileForm, Model model) {
        // Get the current logged in user details
        this.fileService.trackLoggedInUserId(authentication.getName());

        String filename = StringUtils.getFilename(file.getOriginalFilename());
        fileForm.setFilename(filename);
        fileForm.setFileType(file.getContentType());
        fileForm.setFileSize("" + file.getSize());
        try{
            fileForm.setFileData(file.getBytes());
        } catch(Exception e){
            System.out.println("Error reading filedata");
        }

        this.fileService.uploadFile(fileForm);

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);

        return "home";
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String filename, Authentication authentication, FileForm fileForm, Model model){
        this.fileService.trackLoggedInUserId(authentication.getName());
        System.out.println("fileid " + this.fileService.getFile(filename).getFileId());
        System.out.println("filetype " + this.fileService.getFile(filename).getContentType());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(this.fileService.getFile(filename).getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileService.getFile(filename).getFilename() + "\"")
                .body(new ByteArrayResource(fileService.getFile(filename).getFileData()));
    }

    @GetMapping("/files/delete/{fileid}")
    public String deleteFile(@PathVariable("fileid") Integer fileId, Authentication authentication, CredentialForm credentialForm, NoteForm noteForm, FileForm fileForm, Model model){
        System.out.println("fileid" + fileId);
        this.fileService.deleteFile(fileId);

        HomeController.getHomeDetails(authentication, model, this.credentialService, this.noteService, this.fileService, this.encryptionService, this.userService);

        return "home";
    }

}
