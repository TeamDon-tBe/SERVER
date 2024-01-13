package com.dontbe.www.DontBeServer.api.notification.service;

import com.dontbe.www.DontBeServer.api.content.dto.response.ContentGetAllByMemberResponseDto;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    @Transactional
    public void readNotification(Long memberId) {
        Member targetMember = memberRepository.findMemberByIdOrThrow(memberId);
        List<Notification> unreadNotifications = notificationRepository.findAllByNotificationTargetMemberAndIsNotificationChecked(targetMember,false);

        unreadNotifications.forEach(Notification::readNotification);
    }
}
