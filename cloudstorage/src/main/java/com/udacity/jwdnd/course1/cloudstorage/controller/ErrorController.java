package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.ErrorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ErrorController {
    private ErrorService errorService;

    //TODO need to be renamed to resultcontroller or something similar

    public ErrorController(ErrorService errorService){
        this.errorService = errorService;
    }

    @PostMapping("result")
    public String error(String error, Model model){
        if (error.equals("")){
            this.errorService.uploadSuccess();
        } else{
            this.errorService.setErrorMsg(error);
        }
        model.addAttribute("error", this.errorService.getError());
        return "result";
    }
}
