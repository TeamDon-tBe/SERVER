package com.dontbe.www.DontBeServer.common.config.jwt;


import com.dontbe.www.DontBeServer.common.exception.UnAuthorizedException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dontbe.www.DontBeServer.common.config.jwt.JwtExceptionType.*;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request);
        System.out.println("여기까지는 왔는가?필터 도달");
        if (request.getRequestURI().equals("/api/v1/auth/token")) {
            String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

            if (jwtTokenProvider.validateToken(refreshToken) == JwtExceptionType.EMPTY_JWT || jwtTokenProvider.validateToken(accessToken) == JwtExceptionType.EMPTY_JWT) {
                jwtAuthenticationEntryPoint.setResponse(response, ErrorStatus.NO_TOKEN);
                return;
            } else if (jwtTokenProvider.validateToken(accessToken) == JwtExceptionType.EXPIRED_JWT_TOKEN) {
                if (jwtTokenProvider.validateToken(refreshToken) == JwtExceptionType.EXPIRED_JWT_TOKEN) {
                    // access, refresh 둘 다 만료
                    jwtAuthenticationEntryPoint.setResponse(response, ErrorStatus.SIGNIN_REQUIRED);
                    return;
                } else if (jwtTokenProvider.validateToken(refreshToken) == VALID_JWT_TOKEN) {
                    // 토큰 재발급
                    Long memberId = jwtTokenProvider.validateMemberRefreshToken(accessToken, refreshToken);
                    Authentication authentication = new UserAuthentication(memberId, null, null);

                    String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

                    setAuthentication(newAccessToken);
                    request.setAttribute("newAccessToken", newAccessToken);
                }
            } else if (jwtTokenProvider.validateToken(accessToken) == VALID_JWT_TOKEN) {
                jwtAuthenticationEntryPoint.setResponse(response, ErrorStatus.VALID_ACCESS_TOKEN);
                return;
            } else {
                throw new UnAuthorizedException(ErrorStatus.UNAUTHORIZED_TOKEN.getMessage());
            }
        }
        else {
            if (accessToken != null) {
                // 토큰 검증
                if (jwtTokenProvider.validateToken(accessToken) == VALID_JWT_TOKEN) {     // 토큰이 존재하고 유효한 토큰일 때만
                    Long memberId = jwtTokenProvider.getAccessTokenPayload(accessToken);
                    UserAuthentication authentication = new UserAuthentication(memberId, null, null);       //사용자 객체 생성
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // request 정보로 사용자 객체 디테일 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String jwtToken) {
        Long userId = jwtTokenProvider.getAccessTokenPayload(jwtToken);
        Authentication authentication = new UserAuthentication(userId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
