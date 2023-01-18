package com.project.matchingsystem.dto;

import com.project.matchingsystem.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseStatusDto {

    // 메시지

    private final String httpStatus;
    private final String message;

    public ResponseStatusDto(ErrorCode errorCode) {
        httpStatus = String.valueOf(errorCode.getHttpStatus());
        message = errorCode.getMessage();
    }

}
