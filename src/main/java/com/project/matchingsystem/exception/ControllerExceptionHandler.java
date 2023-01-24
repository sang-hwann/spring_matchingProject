package com.project.matchingsystem.exception;

import com.project.matchingsystem.dto.response.ResponseStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseStatusDto handleSQLIntegrityConstraintViolationException() {
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), ErrorCode.NOT_EMPTY_CATEGORY.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseStatusDto handleHttpMessageNotReadableException() {
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), ErrorCode.NOT_READABLE_JSON.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseStatusDto handleHttpMediaTypeNotSupportedException() {
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), ErrorCode.NOT_SUPPORTED_HTTP_MEDIA_TYPE.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseStatusDto handleHttpMediaTypeNotAcceptableException() {
        return new ResponseStatusDto(HttpStatus.BAD_REQUEST.toString(), ErrorCode.NOT_ACCEPTABLE_HTTP_MEDIA_TYPE.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseStatusDto handleRuntimeException(Exception e) {
        log.info("Internal Server Error", e);
        return new ResponseStatusDto(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

}
