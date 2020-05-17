package com.example.translator.controller;

import com.example.translator.service.TranslationService;
import com.example.translator.service.serviceImpl.TranslationServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TranslateController {

    private final TranslationService service;

    public TranslateController(TranslationServiceImpl service) {
        this.service = service;
    }

    @PostMapping(value = "/translate",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void getTranslation(){
        service.getTranslationDB();
    }

}
