package com.application.alledrogo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAcceptableException extends ResponseStatusException {

    public NotAcceptableException(String reason) {
        super(HttpStatus.NOT_ACCEPTABLE, reason);
    }
}
