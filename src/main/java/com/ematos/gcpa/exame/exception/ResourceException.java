package com.ematos.gcpa.exame.exception;


import org.springframework.http.HttpStatus;

/**
 * This interface represents a HTTP Resource Exception.
 * <p>
 * By implementing it, the class is telling that it has a HTTP status associated to it.
 *
 */
public interface ResourceException {
    /**
     * Method responsible for getting the @{link HttpStatus} of this Exception
     *
     * @return a {@link HttpStatus}
     */
    public HttpStatus getStatus();
}