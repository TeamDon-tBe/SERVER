package com.dontbe.www.DontBeServer.api.notification.domain;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notification extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    private Member notificationTargetMember;

    private Long notificationTriggerMemberId;

    private String notificationTriggerType;

    private Long notificationTriggerId;

    private boolean isNotificationChecked;

    private String NotificationText;

    @Builder
    public Notification(Member notificationTargetMember, Long notificationTriggerMemberId, String notificationTriggerType, Long notificationTriggerId, boolean isNotificationChecked, String notificationText) {
        this.notificationTargetMember = notificationTargetMember;
        this.notificationTriggerMemberId = notificationTriggerMemberId;
        this.notificationTriggerType = notificationTriggerType;
        this.notificationTriggerId = notificationTriggerId;
        this.isNotificationChecked = isNotificationChecked;
        this.NotificationText = notificationText;
    }

    public void readNotification(){
        this.isNotificationChecked = true;
    }
}
