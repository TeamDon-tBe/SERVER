package com.dontbe.www.DontBeServer.api.notification.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByNotificationTargetMemberAndNotificationTriggerMemberIdAndNotificationTriggerTypeAndNotificationTriggerId(
            Member notificaitonTargetMember, Long notificationTriggerMemberId, String notificationTriggerType, Long notificationTriggerId
    );
}
