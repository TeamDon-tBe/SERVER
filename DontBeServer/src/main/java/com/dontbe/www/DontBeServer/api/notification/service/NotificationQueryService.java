package com.dontbe.www.DontBeServer.api.notification.service;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificaitonCountResponseDto;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
}
