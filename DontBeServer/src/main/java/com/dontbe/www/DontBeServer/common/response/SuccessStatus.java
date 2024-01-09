package com.dontbe.www.DontBeServer.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * auth
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
    SIGNIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    GET_NEW_TOKEN_SUCCESS(HttpStatus.OK,"토큰 재발급 성공"),
    DRAWAL_SUCCESS(HttpStatus.OK,"유저 탈퇴 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}

