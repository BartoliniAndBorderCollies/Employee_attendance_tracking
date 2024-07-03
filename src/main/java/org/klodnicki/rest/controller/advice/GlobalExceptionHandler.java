package org.klodnicki.rest.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("You didn't provided all necessary information in request! " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleDuplicatedEntry(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>("This entry already exist: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
