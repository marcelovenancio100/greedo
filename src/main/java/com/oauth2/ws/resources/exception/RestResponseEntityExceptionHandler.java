package com.oauth2.ws.resources.exception;

import com.oauth2.ws.services.exception.ObjectAlreadyExistException;
import com.oauth2.ws.services.exception.ObjectNotEnabledException;
import com.oauth2.ws.services.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    //Error 404
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> ObjectNotFound(ObjectNotFoundException e, HttpServletRequest req) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Não encontrado", e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    //Error 409
    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<Object> handleObjectAlreadyExist(final RuntimeException e, HttpServletRequest req) {

        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "UserAlreadyExist", e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    //Error 401
    @ExceptionHandler(ObjectNotEnabledException.class)
    public ResponseEntity<Object> handleObjectNotEnabled(final RuntimeException e, HttpServletRequest req) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "UserNotEnabled", e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
