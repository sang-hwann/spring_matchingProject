package com.sparta.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),

    // 유저 관련 ErrorCode
    AUTHORIZATION(HttpStatus.BAD_REQUEST, "작성자만 수정/삭제할 수 있습니다."),
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "중복된 username 입니다"),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "username과 password의 형식이 올바르지 않습니다."),
    NOT_MATCH_INFORMATION(HttpStatus.BAD_REQUEST, "회원정보가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),

    // 상품 관련 ErrorCode
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    NOT_EXIST_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
