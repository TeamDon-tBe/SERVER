package com.dontbe.www.DontBeServer.api.member.domain;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.auth.SocialPlatform;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.report.domain.Report;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
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

    @Column(name = "is_alarm_allowed")
    private boolean isAlarmAllowed;

    @Column(nullable = false, name = "social_id")
    private String socialId;

    @Column(name= "member_ghost")
    private int memberGhost;

    @Column(name = "member_intro")
    private String memberIntro;

    @Column(name = "profile_url")
    private String profileUrl;

    @OneToMany(mappedBy = "notificationTargetMember",cascade = ALL)
    private List<Notification> targetNotification = new ArrayList<>();

    @OneToMany(mappedBy = "ghostTargetMember", cascade = ALL)
    private List<Ghost> targetGhosts = new ArrayList<>();

    @OneToMany(mappedBy = "ghostTriggerMember", cascade = ALL)
    private List<Ghost> triggerGhost = new ArrayList<>();

    @OneToMany(mappedBy = "reportTargetMember", cascade = ALL)
    private List<Report> targetReport = new ArrayList<>();

    @Builder
    private Member(String nickname, SocialPlatform socialPlatform, String socialId,String profileUrl) {
        this.nickname = nickname;
        this.socialId = socialId;
        this.socialPlatform = socialPlatform;
        this.profileUrl = profileUrl;
    }

//    public static Member create() {
//        return Member.builder()
//                .nickname()
//                .socialId()
//                .socialPlatform()
//                .profileUrl()
//    }

    public void updateRefreshToken (String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
