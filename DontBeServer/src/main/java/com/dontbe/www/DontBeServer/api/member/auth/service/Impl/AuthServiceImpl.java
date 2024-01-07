package com.dontbe.www.DontBeServer.api.member.auth.service.Impl;

import com.dontbe.www.DontBeServer.api.member.auth.SocialPlatform;
import com.dontbe.www.DontBeServer.api.member.auth.dto.*;
import com.dontbe.www.DontBeServer.api.member.auth.dto.request.AuthRequestDto;
import com.dontbe.www.DontBeServer.api.member.auth.dto.response.*;
import com.dontbe.www.DontBeServer.api.member.auth.service.AuthService;
import com.dontbe.www.DontBeServer.api.member.auth.service.KakaoAuthService;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.dontbe.www.DontBeServer.common.config.jwt.JwtTokenProvider;
import com.dontbe.www.DontBeServer.common.config.jwt.UserAuthentication;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthService kakaoAuthService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public AuthResponseDto socialLogin(String socialAccessToken, AuthRequestDto authRequestDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (authRequestDto.getSocialPlatform() == null) {
            throw new BadRequestException(ErrorStatus.VALIDATION_REQUEST_MISSING_EXCEPTION.getMessage());
        }
        SocialPlatform socialPlatform = SocialPlatform.valueOf(authRequestDto.getSocialPlatform());
        SocialInfoDto socialData = getSocialData(socialPlatform, socialAccessToken);
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        Boolean isExistUser = isMemberBySocialId(socialData.getId());

        try {
            // 신규 유저 저장
            if (!isExistUser.booleanValue()) {
                Member member = Member.builder()
                        .nickname(socialData.getNickname())
                        .socialPlatform(socialPlatform)
                        .socialId(socialData.getId())
                        .profileUrl(socialData.getProfileUrl())
                        .build();

                memberRepository.save(member);

                Authentication authentication = new UserAuthentication(member.getId(), null, null);

                String accessToken = jwtTokenProvider.generateAccessToken(authentication);
                member.updateRefreshToken(refreshToken);

                return AuthResponseDto.of(member.getNickname(), member.getId(), accessToken, refreshToken, socialData.getProfileUrl(), true);

            }
            else {
                findMemberBySocialId(socialData.getId()).updateRefreshToken(refreshToken);

                // socialId를 통해서 등록된 유저 찾기
                Member signedMember = findMemberBySocialId(socialData.getId());

                Authentication authentication = new UserAuthentication(signedMember.getId(), null, null);

                String accessToken = jwtTokenProvider.generateAccessToken(authentication);

                return AuthResponseDto.of(signedMember.getNickname(), signedMember.getId(), accessToken, refreshToken, signedMember.getProfileUrl(), false);
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ErrorStatus.ANOTHER_ACCESS_TOKEN.getMessage());
        }
    }

    @Override
    @Transactional
    public AuthTokenResponseDto getNewToken(String accessToken, String refreshToken) {
        return AuthTokenResponseDto.of(accessToken,refreshToken);
    }

    private Member findMemberBySocialId(String socialId) {
        return memberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.INVALID_MEMBER.getMessage()));
    }

    private boolean isMemberBySocialId(String socialId) {
        return memberRepository.existsBySocialId(socialId);
    }

    private SocialInfoDto getSocialData(SocialPlatform socialPlatform, String socialAccessToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        switch (socialPlatform) {
            case KAKAO:
                return kakaoAuthService.login(socialAccessToken);
            default:
                throw new IllegalArgumentException(ErrorStatus.ANOTHER_ACCESS_TOKEN.getMessage());
        }
    }
}
