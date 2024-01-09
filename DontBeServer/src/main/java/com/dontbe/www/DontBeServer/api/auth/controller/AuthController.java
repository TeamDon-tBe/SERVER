package com.dontbe.www.DontBeServer.api.auth.controller;

import com.dontbe.www.DontBeServer.api.auth.dto.request.AuthRequestDto;
import com.dontbe.www.DontBeServer.api.auth.dto.response.AuthResponseDto;
import com.dontbe.www.DontBeServer.api.auth.dto.response.AuthTokenResponseDto;
import com.dontbe.www.DontBeServer.api.auth.service.AuthService;
import com.dontbe.www.DontBeServer.common.config.jwt.JwtTokenProvider;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.response.SuccessStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public ApiResponse<AuthResponseDto>  socialLogin(@RequestHeader("Authorization") String socialAccessToken, @RequestBody AuthRequestDto authRequestDto) throws NoSuchAlgorithmException, InvalidKeySpecException {

        AuthResponseDto responseDto = authService.socialLogin(socialAccessToken, authRequestDto);
        // 로그인
        if (!responseDto.getIsNewUser()) {
            return ApiResponse.success(SuccessStatus.SIGNIN_SUCCESS, responseDto);
        }
        // 회원가입
        return ApiResponse.success(SuccessStatus.SIGNUP_SUCCESS, responseDto);

    }

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthTokenResponseDto> getNewToken(HttpServletRequest request) {
        String accessToken = (String) request.getAttribute("newAccessToken");
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        return ApiResponse.success(SuccessStatus.GET_NEW_TOKEN_SUCCESS, authService.getNewToken(accessToken, refreshToken));
    }
}
