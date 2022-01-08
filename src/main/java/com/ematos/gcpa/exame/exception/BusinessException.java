package com.ematos.gcpa.exame.exception;

import org.springframework.http.HttpStatus;

/**
 * Generic Business Exception
 */
public class BusinessException extends RuntimeException implements ResourceException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Exception ex) {
        super(ex);
    }

    public BusinessException(String msg, Exception ex) {
        super(msg, ex);
    }

    public BusinessException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}