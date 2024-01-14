package com.dontbe.www.DontBeServer.api.notification.service;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificaitonCountResponseDto;
import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificationAllResponseDto;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationQueryService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    public NotificaitonCountResponseDto countUnreadNotification(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int number = notificationRepository.countByNotificationTargetMemberAndIsNotificationChecked(member, false);
        return NotificaitonCountResponseDto.of(number);
    }

    public List<NotificationAllResponseDto> getNotificationAll(Long memberId, Long targetMemberId){
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        Member targetMember = memberRepository.findMemberByIdOrThrow(targetMemberId);
        List<Notification> notificationList = notificationRepository.findNotificationsByNotificationTargetMember(targetMember);

        //System.out.println("usingMemberId : "+ usingMember.getId().toString());

        //댓글알림이면 triggerId > contentId반환추가하기
        return notificationList.stream()
                .map(oneNotification -> NotificationAllResponseDto.of(
                        usingMember,
                        memberRepository.findMemberByIdOrThrow(oneNotification.getNotificationTriggerMemberId()),
                        oneNotification,
                        oneNotification.isNotificationChecked()
                )).collect(Collectors.toList());
    }
}
