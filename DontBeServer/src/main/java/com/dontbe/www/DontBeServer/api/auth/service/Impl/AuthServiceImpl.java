package com.dontbe.www.DontBeServer.api.auth.service.Impl;

import com.dontbe.www.DontBeServer.api.auth.SocialPlatform;
import com.dontbe.www.DontBeServer.api.auth.dto.SocialInfoDto;
import com.dontbe.www.DontBeServer.api.auth.dto.response.AuthResponseDto;
import com.dontbe.www.DontBeServer.api.auth.dto.response.AuthTokenResponseDto;
import com.dontbe.www.DontBeServer.api.auth.dto.request.AuthRequestDto;
import com.dontbe.www.DontBeServer.api.auth.service.AppleAuthService;
import com.dontbe.www.DontBeServer.api.auth.service.AuthService;
import com.dontbe.www.DontBeServer.api.auth.service.KakaoAuthService;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.dontbe.www.DontBeServer.common.config.jwt.JwtTokenProvider;
import com.dontbe.www.DontBeServer.common.config.jwt.UserAuthentication;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final static String GHOST_IMAGE = "https://github.com/TeamDon-tBe/SERVER/assets/97835512/fb3ea04c-661e-4221-a837-854d66cdb77e";

    @Value("${aws-property.s3-default-image-url}")
    private String GHOST_IMAGE_S3;
    private final static String DEFAULT_NICKNAME="";

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthService kakaoAuthService;
    private final AppleAuthService appleAuthService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public AuthResponseDto socialLogin(String socialAccessToken, AuthRequestDto authRequestDto) throws NoSuchAlgorithmException, InvalidKeySpecException {

        val socialPlatform = SocialPlatform.valueOf(authRequestDto.getSocialPlatform());
        SocialInfoDto socialData = getSocialData(socialPlatform, socialAccessToken, authRequestDto.getUserName());
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        Boolean isExistUser = isMemberBySocialId(socialData.getId());


        try {
            // 신규 유저 저장
            if (!isExistUser.booleanValue()) {
                Member member = Member.builder()
                        .nickname(DEFAULT_NICKNAME)//.nickname(socialData.getNickname())
                        .socialPlatform(socialPlatform)
                        .socialId(socialData.getId())
                        .profileUrl(GHOST_IMAGE)
                        .memberEmail(socialData.getEmail())
                        .socialNickname(socialData.getNickname())
                        .build();

                memberRepository.save(member);

                Authentication authentication = new UserAuthentication(member.getId(), null, null);

                String accessToken = jwtTokenProvider.generateAccessToken(authentication);
                member.updateRefreshToken(refreshToken);

                return AuthResponseDto.of(member.getNickname(), member.getId(), accessToken, refreshToken, member.getProfileUrl(), true);

            }
            else {

                Boolean isDeleted = memberRepository.findMemberBySocialId(socialData.getId()).isDeleted();

                //재가입 방지
                if(isExistUser && isDeleted){
                    throw new BadRequestException(ErrorStatus.WITHDRAWAL_MEMBER.getMessage());
                }

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

    private SocialInfoDto getSocialData(SocialPlatform socialPlatform, String socialAccessToken, String userName) {

        switch (socialPlatform) {
            case KAKAO:
                return kakaoAuthService.login(socialAccessToken);
            case APPLE:
                return appleAuthService.login(socialAccessToken, userName);
            case WITHDRAW:
                return null;
            default:
                throw new IllegalArgumentException(ErrorStatus.ANOTHER_ACCESS_TOKEN.getMessage());
        }
    }
}
