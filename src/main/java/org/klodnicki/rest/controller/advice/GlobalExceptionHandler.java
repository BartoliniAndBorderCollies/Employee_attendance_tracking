package org.klodnicki.rest.controller.advice;

import org.klodnicki.dto.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseDTO("You didn't provided all necessary information in request! " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseDTO handleDuplicatedEntry(SQLIntegrityConstraintViolationException e) {
        return new ResponseDTO("This entry already exist: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundInDatabaseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO handleNotFoundInDatabaseException(NotFoundInDatabaseException e) {
        return new ResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
