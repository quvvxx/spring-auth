package com.cy.auth.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다."),

    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용중인 닉네임 입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
