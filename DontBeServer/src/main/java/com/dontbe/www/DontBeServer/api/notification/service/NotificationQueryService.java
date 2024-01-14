package com.dontbe.www.DontBeServer.api.notification.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final String SYSTEM_PROFILEURL = "https://github.com/TeamDon-tBe/SERVER/assets/128011308/327d416e-ef1f-4c10-961d-4d9b85632d87";

    public NotificaitonCountResponseDto countUnreadNotification(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int number = notificationRepository.countByNotificationTargetMemberAndIsNotificationChecked(member, false);
        return NotificaitonCountResponseDto.of(number);
    }

    public List<NotificationAllResponseDto> getNotificationAll(Long memberId){
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        System.out.println("1");
        List<Notification> notificationList = notificationRepository.findNotificationsByNotificationTargetMember(usingMember);

        return notificationList.stream()
                .map(oneNotification -> NotificationAllResponseDto.of(
                        usingMember,
                        memberRepository.findMemberByIdOrThrow(oneNotification.getNotificationTriggerMemberId()),
                        oneNotification,
                        oneNotification.isNotificationChecked(),
                        notificationTriggerId(oneNotification.getNotificationTriggerType(),
                                oneNotification.getNotificationTriggerId(), oneNotification),
                        profileUrl(oneNotification.getId(), oneNotification.getNotificationTriggerType())
                )).collect(Collectors.toList());
    }

    private long notificationTriggerId (String triggerType, Long triggerId, Notification notification){

        Comment comment = commentRepository.findCommentByIdOrThrow(triggerId);
        System.out.println("2");
        //답글관련(답글좋아요 혹은 답글 작성)시 게시물 id 반환
        if(triggerType.equals("comment") || triggerType.equals("commentLiked")){
            System.out.println("3");
            return comment.getContent().getId();
        }else {
            System.out.println("4");
            return notification.getNotificationTriggerId();}
    }

    private String profileUrl(Long notificationId, String triggerType){
        Notification notification = notificationRepository.findNotificationById(notificationId);
        Member triggerMember = memberRepository.findMemberByIdOrThrow(notification.getNotificationTriggerMemberId());
        System.out.println("5");
        if(triggerType.equals("comment") || triggerType.equals("commentLiked") || triggerType.equals("contentLiked")){
            return triggerMember.getProfileUrl();
        }else{
            return SYSTEM_PROFILEURL;
        }
    }
}
