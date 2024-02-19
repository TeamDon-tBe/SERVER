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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {

    private static final long ACCOUNT_RETENTION_PERIOD = 30L;   // 계정 삭제 후 보유기간 30일로 설정

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

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "social_nickname")
    private String socialNickname;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @Column(name = "deleted_At")
    private LocalDateTime deleteAt;

    @Column(name = "deleted_Reason")
    private String deletedReason;

    @OneToMany(mappedBy = "notificationTargetMember",cascade = ALL)
    private List<Notification> targetNotification = new ArrayList<>();

    @OneToMany(mappedBy = "ghostTargetMember", cascade = ALL)
    private List<Ghost> targetGhosts = new ArrayList<>();

    @OneToMany(mappedBy = "ghostTriggerMember", cascade = ALL)
    private List<Ghost> triggerGhost = new ArrayList<>();

    @OneToMany(mappedBy = "reportTargetMember", cascade = ALL)
    private List<Report> targetReport = new ArrayList<>();

    @Builder
    private Member(String nickname, SocialPlatform socialPlatform, String socialId, String profileUrl, String memberEmail, String socialNickname) {
        this.nickname = nickname;
        this.socialId = socialId;
        this.socialPlatform = socialPlatform;
        this.profileUrl = profileUrl;
        this.memberIntro = "";
        this.memberGhost = 0;
        this.memberEmail = memberEmail;
        this.socialNickname = socialNickname;
    }

    public void decreaseGhost() {
        this.memberGhost--;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateProfileUrl(String newProfileUrl) {
        this.profileUrl = newProfileUrl;
    }

    public void updateMemberIntro(String newMemberIntro) {
        this.memberIntro = newMemberIntro;
    }

    public void updateMemberIsAlarmAllowed(boolean newIsAlarmAllowed) {
        this.isAlarmAllowed = newIsAlarmAllowed;
    }

    public void updateRefreshToken (String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateDeletedReason(String withdrawlReason){
        this.deletedReason = withdrawlReason;
    }
    public void softDelete() {
        this.isDeleted = true;
        this.deleteAt = LocalDateTime.now().plusDays(ACCOUNT_RETENTION_PERIOD);
    }
}
