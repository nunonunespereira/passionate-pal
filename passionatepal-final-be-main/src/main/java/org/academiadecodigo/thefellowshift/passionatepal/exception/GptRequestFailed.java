package org.academiadecodigo.thefellowshift.passionatepal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class GptRequestFailed extends ResponseStatusException {
    public GptRequestFailed(String message) {
        super(HttpStatus.SERVICE_UNAVAILABLE, message);
    }
}
