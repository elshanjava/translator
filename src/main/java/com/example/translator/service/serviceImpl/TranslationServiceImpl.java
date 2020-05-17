package com.example.translator.service.serviceImpl;

import com.example.translator.exception.BadRequestException;
import com.example.translator.exception.NoExistElementException;
import com.example.translator.model.Constant;
import com.example.translator.model.TranslationEntity;
import com.example.translator.model.TranslationResponse;
import com.example.translator.repository.TranslationRepo;
import com.example.translator.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

@Service
@Slf4j
public class TranslationServiceImpl implements TranslationService {
    @Value("${yandex.api.key}")
    private String apiKey;

    @Value("${yandex.base.url}")
    private String baseUrl;

    private final RestTemplate template;

    private final TranslationRepo translationRepo;

    public TranslationServiceImpl(RestTemplate template, TranslationRepo translationRepo) {
        this.template = template;
        this.translationRepo = translationRepo;
    }

    @Override
    public Future<ResponseEntity<TranslationResponse>> getTranslation(String text, String lang)
    {
        if (lang.isEmpty() || lang.length()<5 || lang.length()>7){
            log.warn("Language is not correctly");
            throw new BadRequestException();
        }
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(Constant.TRANSLATION_URL);
        url.append(Constant.KEY).append(Constant.EQUAL_SIGN).append(apiKey).append(Constant.AMPERSAND_SIGN);
        url.append(Constant.TEXT_TO_TRANSLATE).append(Constant.EQUAL_SIGN).append(text).append(Constant.AMPERSAND_SIGN);
        url.append(Constant.TARGET_LANGUAGE).append(Constant.EQUAL_SIGN).append(lang);
        ResponseEntity<TranslationResponse> translation = template.getForEntity(url.toString(), TranslationResponse.class );
        log.info("{} was translate. Type: {}", text, lang);
        return new AsyncResult<>(translation);
    }

    @Override
    public void getTranslationDB() {
        List<TranslationEntity> entityFromDB = translationRepo.findAll();
        if (entityFromDB.isEmpty()){
            log.warn("Database is empty");
            throw new NoExistElementException();
        }
        entityFromDB.forEach(db->{
           Future<ResponseEntity<TranslationResponse>> response =
                   getTranslation(db.getText(), db.getTranslateFrom().concat("-").concat(db.getTranslateTo()));
            ResponseEntity<TranslationResponse> translation = null;
            try {
                translation = response.get();
            } catch (Exception e) {
                log.info("Interrupted exception");
                e.printStackTrace();
            }
            assert translation != null;
            if (HttpStatus.OK.value() <= translation.getStatusCode().value() && translation.getStatusCode().value() < HttpStatus.MULTIPLE_CHOICES.value()) {
                    db.setResult(Objects.requireNonNull(translation.getBody()).getText().get(0));
                    translationRepo.save(db);
                    log.info("{} was save with result {}", db.getText(), db.getResult() );
            }

        });

    }

}
