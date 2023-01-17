package com.sparta.matchingsystem.exception;

import com.sparta.matchingsystem.dto.ResponseStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseStatusDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseStatusDto handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseStatusDto handleRuntimeException(Exception e) {
        return new ResponseStatusDto(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
    }

}
