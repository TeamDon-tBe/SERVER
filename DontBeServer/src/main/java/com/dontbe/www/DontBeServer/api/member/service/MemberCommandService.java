package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.domain.CommentLiked;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentLikedRepository;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.domain.ContentLiked;
import com.dontbe.www.DontBeServer.api.content.repository.ContentLikedRepository;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final GhostRepository ghostRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final CommentLikedRepository commentLikedRepository;
    private final ContentLikedRepository contentLikedRepository;

    public void withdrawalMember(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }

    public void testWithdrawalMember(Long memberId){
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        List<Content> contentList = contentRepository.findContentByMember(member);
        List<Ghost> ghostList1 = ghostRepository.findByGhostTargetMember(member);
        List<Ghost> ghostList2 = ghostRepository.findByGhostTriggerMember(member);
        System.out.println("들어옴");
        //게시글들 안에서 각 게시글에 대한 답글들의 좋아요 노티, 투명도 노티, 답글 노티, 답글 삭제 + 게시글 좋아요 삭제, 게시글 투명도 삭제, 게시글 삭제(소프트 딜리트 X)
        for(Content content : contentList) {
            Long contentId = content.getId();
            List<Comment> comments = commentRepository.findCommentsByContentId(contentId);
            for(Comment comment : comments) {
                notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("commentLiked",comment.getId());
//            notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("commentGhost",comment.getId());   //변경 후 다시 바꾸기
                notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("commentGhost",contentId);
                notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("comment", comment.getId());
                commentLikedRepository.deleteByComment(comment);
                commentRepository.deleteById(comment.getId());
            }
            System.out.println("1");
            notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("contentLiked",contentId);
            System.out.println("2");
            notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("contentGhost",contentId);
            System.out.println("3");
            contentRepository.deleteById(contentId);
            System.out.println("4");
        }

        //투명도 관련 운영 노티는 위에서 삭제되지 않기 때문에 아래 과정을 통해서 삭제되게끔 했습니다.
        //target일 경우랑 trigger일 경우, 두 가지 경우에 대해서 모두 진행한 이유는 혹시 몰라서입니다.
        List<Notification> notification1 = notificationRepository.findAllByNotificationTargetMember(member);
        System.out.println("5");
        List<Notification> notification2 = notificationRepository.findAllByNotificationTriggerMemberId(memberId);
        System.out.println("6");
        notificationRepository.deleteAll(notification1);
        System.out.println("가나라다라");
        notificationRepository.deleteAll(notification2);
        System.out.println("마바사");

        //초기 앱심사를 위해서 만든 탈퇴이기 때문에 탈퇴하는 유저가 누른 투명도에 대해 상대방을 회복 시킬지 말지에 대한 부분은 생각하지 않고
        //그냥 삭제되는 것으로 일단 진행하겠습니다. (p.s 이전에 카톡으로 추후에는 soft delete 적용하기로함!)
        //이대로 진행할 경우 어떤 유저는 평생 default값이 -1일 수도ㅜㅜ
        ghostRepository.deleteAll(ghostList1);
        System.out.println("고스트리스트1");
        ghostRepository.deleteAll(ghostList2);
        System.out.println("고스트리스트2");
//        contentRepository.deleteAll(contentList);

        //좋아요 삭제
        List<ContentLiked> contentLiked = contentLikedRepository.findAllByMemberId(memberId);
        List<CommentLiked> commentLiked = commentLikedRepository.findAllByMemberId(memberId);
        contentLikedRepository.deleteAll(contentLiked);
        commentLikedRepository.deleteAll(commentLiked);
        memberRepository.delete(member);
    }

    public void updateMemberProfile(Long memberId, MemberProfilePatchRequestDto memberProfilePatchRequestDto) {
        Member existingMember = memberRepository.findMemberByIdOrThrow(memberId);

        // 업데이트할 속성만 복사
        if (memberProfilePatchRequestDto.nickname() != null) {
            existingMember.updateNickname(memberProfilePatchRequestDto.nickname());
        }
        if (memberProfilePatchRequestDto.member_intro() != null) {
            existingMember.updateMemberIntro(memberProfilePatchRequestDto.member_intro());
        }
        if (memberProfilePatchRequestDto.profile_url() != null) {
            existingMember.updateProfileUrl(memberProfilePatchRequestDto.profile_url());
        }
        if (memberProfilePatchRequestDto.is_alarm_allowed() != null) {
            existingMember.updateMemberIsAlarmAllowed(memberProfilePatchRequestDto.is_alarm_allowed());
        }

        // 저장
        Member savedMember = memberRepository.save(existingMember);
    }
}
