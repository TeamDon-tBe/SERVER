package com.dontbe.www.DontBeServer.api.member.domain;

import com.dontbe.www.DontBeServer.api.member.auth.SocialPlatform;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_platform")
    private SocialPlatform socialPlatform;

    @Column(name = "is_fcm_allowed")
    private boolean isFcmAllowed;

    @Column(nullable = false, name = "social_id")
    private String socialId;

    @Column(name= "member_ghost")
    private int memberGhost;

    @Column(name = "member_intro")
    private String memberIntro;

    @Column(name = "profile_url")
    private String profileUrl;

    @Builder
    public Member(String nickname, SocialPlatform socialPlatform, String socialId,String profileUrl) {
        this.nickname = nickname;
        this.socialId = socialId;
        this.socialPlatform = socialPlatform;
        this.profileUrl = profileUrl;
    }

    public void updateRefreshToken (String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
