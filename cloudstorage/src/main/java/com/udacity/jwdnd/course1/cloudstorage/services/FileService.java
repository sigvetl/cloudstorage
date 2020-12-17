package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;
    private UserService userService;
    private Integer userId;

    public FileService(FileMapper fileMapper, UserService userService){
        this.fileMapper = fileMapper;
        this.userService = userService;
        this.userId = null;
    }

    public void uploadFile(FileForm fileForm){
        File file = new File();
        file.setUserId(this.userId);
        file.setFilename(fileForm.getFilename());
        file.setContentType(fileForm.getContentType());
        file.setFileSize(fileForm.getFileSize());
        file.setFileData(fileForm.getFileData());

        this.fileMapper.insertFile(file);
    }

    public void deleteFile(Integer fileId){
        this.fileMapper.deleteFile(fileId);
    }

    public boolean compareFilename(String filename){
        for (File file : this.getAllFiles()){
            if (file.getFilename().equals(filename)){
                return true;
            }
        }
        return false;
    }

    public File getFile(String filename){
        return this.fileMapper.getFile(filename, this.userId);
    }

    public List<File> getAllFiles(){
        return this.fileMapper.getFiles(this.userId);
    }

    public void trackLoggedInUserId(String username){
        this.userId = userService.getUser(username).getUserId();
    }
}
