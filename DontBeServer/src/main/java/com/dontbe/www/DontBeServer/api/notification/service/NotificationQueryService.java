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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    private final int NOTIFICATION_DEFAULT_PAGE_SIZE = 15;

    public NotificaitonCountResponseDto countUnreadNotification(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int number = notificationRepository.countByNotificationTargetMemberAndIsNotificationChecked(member, false);
        return NotificaitonCountResponseDto.of(number);
    }

    public List<NotificationAllResponseDto> getNotificationAll(Long memberId){  //페이지네이션 적용 후 지우기
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        List<Notification> notificationList = notificationRepository.findNotificationsByNotificationTargetMemberOrderByCreatedAtDesc(usingMember);

        return notificationList.stream()
                .map(oneNotification -> NotificationAllResponseDto.of(
                        usingMember,
                        isSystemOrUser(oneNotification.getNotificationTriggerMemberId()),
                        oneNotification,
                        oneNotification.isNotificationChecked(),
                        refineNotificationTriggerId(oneNotification.getNotificationTriggerType(),
                                oneNotification.getNotificationTriggerId(), oneNotification),
                        profileUrl(oneNotification.getId(), oneNotification.getNotificationTriggerType())
                )).collect(Collectors.toList());
    }

    public List<NotificationAllResponseDto> getNotificationAllPagination(Long memberId, Long cursor){
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);

        PageRequest pageRequest = PageRequest.of(0, NOTIFICATION_DEFAULT_PAGE_SIZE);
        Slice<Notification> notificationList;

        if(cursor==-1){
            notificationList = notificationRepository.findTop15ByNotificationTargetMemberOrderByCreatedAtDesc(usingMember, pageRequest);
        }else{
            notificationList = notificationRepository.findNotificationsNextPage(cursor, memberId, pageRequest);
        }

        return notificationList.stream()
                .map(oneNotification -> NotificationAllResponseDto.of(
                        usingMember,
                        isSystemOrUser(oneNotification.getNotificationTriggerMemberId()),
                        oneNotification,
                        oneNotification.isNotificationChecked(),
                        refineNotificationTriggerId(oneNotification.getNotificationTriggerType(),
                                oneNotification.getNotificationTriggerId(), oneNotification),
                        profileUrl(oneNotification.getId(), oneNotification.getNotificationTriggerType())
                )).collect(Collectors.toList());
    }

    private long refineNotificationTriggerId (String triggerType, Long triggerId, Notification notification){

//        Comment comment = commentRepository.findCommentByIdOrThrow(triggerId);
        //답글관련(답글좋아요 혹은 답글 작성, 답글 투명도)시 게시물 id 반환
        if(triggerType.equals("comment") || triggerType.equals("commentLiked") || triggerType.equals("commentGhost")) {
            Comment comment = commentRepository.findCommentByIdOrThrow(triggerId);
            return comment.getContent().getId();
        }else{
            if(triggerType.equals("actingContinue")||triggerType.equals("userBan")) {
                return -1;
            }
            return notification.getNotificationTriggerId();}
    }

    private String profileUrl(Long notificationId, String triggerType){
        if(triggerType.equals("comment") || triggerType.equals("commentLiked") || triggerType.equals("contentLiked")){
            Notification notification = notificationRepository.findNotificationById(notificationId);
            Member triggerMember = memberRepository.findMemberByIdOrThrow(notification.getNotificationTriggerMemberId());
            return triggerMember.getProfileUrl();
        }else{
            return SYSTEM_PROFILEURL;
        }
    }

    //운영 노티의 경우 트리거 유저가 없기 때문에 "System"을 반환하도록 수정
    private String isSystemOrUser(Long memberId) {
        if(memberId != -1) {
            return memberRepository.findMemberByIdOrThrow(memberId).getNickname();
        }
        else return "System";
    }
}
