package com.example.translator.service;

import com.example.translator.model.TranslationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public interface TranslationService {
    Future<ResponseEntity<TranslationResponse>> getTranslation(String text, String lang);
    void getTranslationDB();
}
