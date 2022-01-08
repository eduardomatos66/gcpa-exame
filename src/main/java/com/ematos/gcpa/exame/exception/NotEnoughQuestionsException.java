package com.ematos.gcpa.exame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class NotEnoughQuestionsException extends BusinessException {

    public NotEnoughQuestionsException(String msg) {
        super(msg);
    }
}