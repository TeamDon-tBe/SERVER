package com.dontbe.www.DontBeServer.api.member.auth.service;

import com.dontbe.www.DontBeServer.api.member.auth.dto.request.AuthRequestDto;
import com.dontbe.www.DontBeServer.api.member.auth.dto.response.AuthResponseDto;
import com.dontbe.www.DontBeServer.api.member.auth.dto.response.AuthTokenResponseDto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthService {
    AuthResponseDto socialLogin(String socialAccessToken, AuthRequestDto authRequestDto) throws NoSuchAlgorithmException, InvalidKeySpecException;

    AuthTokenResponseDto getNewToken(String accessToken, String refreshToken);
}


