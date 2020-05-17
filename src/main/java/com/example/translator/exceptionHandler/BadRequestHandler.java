package com.example.translator.exceptionHandler;

import com.example.translator.exception.BadRequestException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BadRequestHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<RequestException> handlerBadRequest(){
        return new ResponseEntity<>(new RequestException("Your request is not correctly"), HttpStatus.BAD_REQUEST);
    }

    @Data
    private static class RequestException {
        public RequestException(String message) {
            this.message=message;
        }

        private String message;
    }

}
