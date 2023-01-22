package com.project.matchingsystem.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseStatusDto {

    private final String httpStatus;
    private final String message;

}
