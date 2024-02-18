package com.dontbe.www.DontBeServer.api.notification.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    //노티 리스트 조회
    @Query("select n FROM Notification n where n.id < ?1 AND n.notificationTargetMember.id = ?2 ORDER BY n.createdAt DESC")
    Slice<Notification> findNotificationsNextPage(Long lastNotificationId, Long memberId, PageRequest pageRequest);

    Slice<Notification> findTop15ByNotificationTargetMemberOrderByCreatedAtDesc(Member memberId, PageRequest pageRequest);

    List<Notification> findAllByNotificationTargetMember(Member member);

    List<Notification> findAllByNotificationTriggerMemberId(Long memberId);
}
