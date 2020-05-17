package com.example.translator.exceptionHandler;

import com.example.translator.exception.NoExistElementException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class NoExistElementHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NoExistElementException.class)
    protected ResponseEntity<ExistElementException> handlerNoSuchElement(){
        return new ResponseEntity<>(new ExistElementException("Your database is empty"), HttpStatus.NOT_FOUND);
    }

    @Data
    private static class ExistElementException {
        public ExistElementException(String message) {
            this.message = message;
        }

        private String message;

    }
}
