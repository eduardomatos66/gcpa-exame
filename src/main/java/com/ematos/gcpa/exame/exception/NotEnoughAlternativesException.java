package com.ematos.gcpa.exame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class NotEnoughAlternativesException extends BusinessException {

    public NotEnoughAlternativesException(String msg) {
        super(msg);
    }
}