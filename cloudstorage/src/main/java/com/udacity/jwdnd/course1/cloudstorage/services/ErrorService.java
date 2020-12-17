package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Error;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
    private UserService userService;
    private Integer userId;
    private Error error = new Error();

    public ErrorService(UserService userService){
        this.userService = userService;
        this.userId = null;
    }

    public void setErrorMsg(String msg){
        error.setMsg(msg);
        error.setSuccess(false);
    }

    public void uploadSuccess(){
        error.setMsg("");
        error.setSuccess(true);
    }

    public Error getError(){
        return error;
    }
}
