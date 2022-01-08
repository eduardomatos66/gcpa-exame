package com.ematos.gcpa.exame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionNotExistentException extends BusinessException {

    public QuestionNotExistentException(String msg) {
        super(msg);
    }
}