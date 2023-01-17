package com.sparta.matchingsystem.dto;

import com.sparta.matchingsystem.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseStatusDto {

    private final String httpStatus;
    private final String message;

    public ResponseStatusDto(ErrorCode errorCode) {
        httpStatus = String.valueOf(errorCode.getHttpStatus());
        message = errorCode.getMessage();
    }

}