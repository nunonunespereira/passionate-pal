package org.academiadecodigo.thefellowshift.passionatepal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
@EnableWebMvc
@Slf4j
public class ControllerAdvisor {



    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleXXException(HttpClientErrorException e) {
        log.error("log HttpClientErrorException: ", e);
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Object> handleXXException(HttpServerErrorException e) {
        log.error("log HttpServerErrorException: ", e);
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error("log unknown error", e);
        return "unknown_error_message";
    }



}
