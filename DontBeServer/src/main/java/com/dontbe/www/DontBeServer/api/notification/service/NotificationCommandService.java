package com.dontbe.www.DontBeServer.api.notification.service;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificaitonCountResponseDto;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationCommandService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    @Transactional
    public void readNotification(Long memberId) {
        Member targetMember = memberRepository.findMemberByIdOrThrow(memberId);
        List<Notification> unreadNotifications = notificationRepository.findAllByNotificationTargetMemberAndIsNotificationChecked(targetMember,false);

        unreadNotifications.forEach(Notification::readNotification);
    }

    public NotificaitonCountResponseDto countUnreadNotification(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int number = notificationRepository.countByNotificationTargetMemberAndIsNotificationChecked(member, false);
        return NotificaitonCountResponseDto.of(number);
    }
}
