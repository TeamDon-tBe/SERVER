package com.dontbe.www.DontBeServer.api.notification.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByNotificationTargetMemberAndNotificationTriggerMemberIdAndNotificationTriggerTypeAndNotificationTriggerId(
            Member notificaitonTargetMember, Long notificationTriggerMemberId, String notificationTriggerType, Long notificationTriggerId
    );

    List<Notification> findAllByNotificationTargetMemberAndIsNotificationChecked(Member member, boolean b);

    int countByNotificationTargetMemberAndIsNotificationChecked(Member member, boolean b);

    List<Notification> findNotificationsByNotificationTargetMemberOrderByCreatedAtDesc(Member member);

    Notification findNotificationById(Long notificationId);

    //게시물에 해당하는 모든 유저의 좋아요 노티 삭제
    void deleteByNotificationTriggerTypeAndNotificationTriggerId(String triggerType, Long triggerId);

//    default Notification findNotificationByIdOrThrow(Long notificationId) {
//        return findNotificationByIdOrThrow(notificationId)
//        orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_NOTIFICATION.getMessage()));
//    }
}
