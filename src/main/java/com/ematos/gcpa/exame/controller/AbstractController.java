package com.ematos.gcpa.exame.controller;

import com.ematos.gcpa.exame.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Abstract Controller which prefixes all endpoints, wraps requests and handles exceptions
 */
public class AbstractController {

    private final Logger logger = Logger.getLogger("CapelloAppController");

    /**
     * Creates a {@link ResponseEntity}.
     * <p>
     * It will contains a 500 HTTP Status (Internal Server Error)
     *
     * @return a {@link ResponseEntity}
     */
    protected ResponseEntity internalServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"mes\":\"Something went wrong. Contact the system's administrator for more information.\"}");
    }

    /**
     * Creates a json message, in the default format, ready to be sent in the response body.
     *
     * @param message message to be sent
     * @return a proper json string with the message in the property "mes"
     */
    protected String createMessageJson(String message) {
        return "{\"mes\":\"" + message + "\"}";
    }

    /**
     * Creates a {@link ResponseEntity}.
     * <p>
     * It will contains a given HTTP status and an object to be set on its body.
     *
     * @param status the HTTP status code to be set.
     * @param obj object to be set on the response body.
     * @return a {@link ResponseEntity}
     */
    protected ResponseEntity response(HttpStatus status, Object obj) {
        return ResponseEntity.status(status).body(obj);
    }

    /**
     * Wraps the result of the supplier into a ResponseEntity
     *
     * @param supplier the supplier to have its result wrapped into the response entity
     * @return the Response Entity with the result of the supplier wrapped into it
     */
    protected <T> ResponseEntity<T> wrapResponseEntity(Supplier<T> supplier) {
        ResponseEntity response;
        try {
            response = ok(supplier.get());
        } catch (BusinessException ex) {
            response = response(ex.getStatus(), createMessageJson(ex.getMessage()));
            logException(response, ex);
        } catch (Exception ex) {
            response = internalServerError();
            logException(response, ex);
        }
        return response;
    }

    /**
     * Wraps the result of the supplier into a ResponseEntity
     *
     * @param supplier the supplier to have its result wrapped into the response entity
     * @return the Response Entity with the result of the supplier wrapped into it
     */
    protected <T> ResponseEntity<T> wrapEmptyBodyResponseEntity(Supplier<T> supplier) {
        ResponseEntity response;
        try {
            supplier.get();
            response = ok().build();
        } catch (BusinessException ex) {
            response = response(ex.getStatus(), createMessageJson(ex.getMessage()));
            logException(response, ex);
        } catch (Exception ex) {
            response = internalServerError();
            logException(response, ex);
        }
        return response;
    }

    /**
     * Logs the exceptions at the API layer.
     *
     * @param response Resulting response of the API component
     * @param ex Resulting exception
     */
    protected void logException(ResponseEntity response, Exception ex) {
        this.logException(getCurrentClassAndMethodName(), response, ex);
    }

    /**
     * Logs the exceptions at the API layer.
     *
     * @param tag Identifier of the object logging the exception
     * @param response Resulting response of the API component
     * @param ex Resulting exception
     */
    protected void logException(String tag, ResponseEntity response, Exception ex) {
        MessageFormat message = new MessageFormat("Request FAILED in {0}\n with response code {1}\n and exception {2}");
        Object[] args = {tag, response.toString(), ex.toString()};
        logger.log(Level.SEVERE, message.format(args), ex);
    }

    private String getCurrentClassAndMethodName() {
        final StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement e = getEnclosingStackTraceElement(stacks);
        String className = "CapelloService";
        String methodName = "";
        if (e != null) {
            className = e.getClassName();
            methodName = e.getMethodName();
        }
        return className + "." + methodName;
    }

    private StackTraceElement getEnclosingStackTraceElement(StackTraceElement[] stacks) {
        int idx = 0;
        StackTraceElement stackTraceElement = null;
        for (StackTraceElement elem : stacks) {
            if (elem.getClassName().equals(this.getClass().getName())) {
                stackTraceElement = stacks[idx];
                break;
            }
            idx++;
        }
        return stackTraceElement;
    }

}