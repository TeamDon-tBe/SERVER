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

    public NotificaitonCountResponseDto countUnreadNotification(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int number = notificationRepository.countByNotificationTargetMemberAndIsNotificationChecked(member, false);
        return NotificaitonCountResponseDto.of(number);
    }

    public List<NotificationAllResponseDto> getNotificationAll(Long memberId, Long targetMemberId){
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        Member targetMember = memberRepository.findMemberByIdOrThrow(targetMemberId);
        List<Notification> notificationList = notificationRepository.findNotificationsByNotificationTargetMember(targetMember);


        return notificationList.stream()
                .map(oneNotification -> NotificationAllResponseDto.of(
                        usingMember,
                        memberRepository.findMemberByIdOrThrow(oneNotification.getNotificationTriggerMemberId()),
                        oneNotification,
                        oneNotification.isNotificationChecked(),
                        notificationTriggerId(oneNotification.getNotificationTriggerType(),
                                oneNotification.getNotificationTargetMember(), oneNotification )
                )).collect(Collectors.toList());
    }

    private long notificationTriggerId (String triggerType, Member targetMember , Notification notification){

        List<Comment> commentList = commentRepository.findCommentByMember(targetMember);
        return notificationList.stream()
                .map(oneCommet->  )

        //답글관련(답글좋아요 혹은 답글 작성)시 게시물 id 반환
        if(triggerType.equals("comment") || triggerType.equals("commentLiked")){
            return comment.getContent().getId();
        }else {return notification.getNotificationTriggerId();}
    }
}
