package com.dontbe.www.DontBeServer.api.content.service;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.domain.ContentLiked;
import com.dontbe.www.DontBeServer.api.content.dto.request.ContentLikedRequestDto;
import com.dontbe.www.DontBeServer.api.content.dto.request.ContentPostRequestDto;
import com.dontbe.www.DontBeServer.api.content.repository.ContentLikedRepository;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.exception.UnAuthorizedException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentCommandService {
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentLikedRepository contentLikedRepository;
    private final NotificationRepository notificationRepository;

    public void postContent(Long memberId, ContentPostRequestDto contentPostRequestDto) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Content content = Content.builder()
                .member(member)
                .contentText(contentPostRequestDto.contentText())
                .build();
        Content savedContent = contentRepository.save(content);
    }

    public void deleteContent(Long memberId, Long contentId) {
        deleteValidate(memberId, contentId);
        contentRepository.deleteById(contentId);
    }

    public void likeContent(Long memberId, Long contentId, ContentLikedRequestDto contentLikedRequestDto) {
        Member triggerMember = memberRepository.findMemberByIdOrThrow(memberId);
        Content content = contentRepository.findContentByIdOrThrow(contentId);

        isDuplicateContentLike(content, triggerMember);

        Member targetMember = memberRepository.findMemberByIdOrThrow(content.getMember().getId());
        ContentLiked contentLiked =  ContentLiked.builder()
                .content(content)
                .member(triggerMember)
                .build();
        ContentLiked savedContentLiked = contentLikedRepository.save(contentLiked);

        //위에가 게시물 좋아요 관련, 아래는 노티 테이블 채우기. 노티에 게시글 내용이 없어서 빈스트링 제공.
        Notification notification = Notification.builder()
                .notificationTargetMember(targetMember)
                .notificationTriggerMemberId(triggerMember.getId())
                .notificationTriggerType(contentLikedRequestDto.alarmTriggerType())
                .notificationTriggerId(contentId)
                .isNotificationChecked(false)
                .notificationText("")
                .build();
        Notification savedNotification = notificationRepository.save(notification);
    }


    private void deleteValidate(Long memberId, Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorStatus.NOT_FOUND_CONTENT.getMessage()));

        Long contentMemberId = content.getMember().getId();
        if (!contentMemberId.equals(memberId)) {
            throw new UnAuthorizedException(ErrorStatus.UNAUTHORIZED_MEMBER.getMessage());
        }
    }

    private void isDuplicateContentLike(Content content, Member member) {
        if(contentLikedRepository.existsByContentAndMember(content,member)) {
            throw new BadRequestException(ErrorStatus.DUPLICATION_CONTENT_LIKE.getMessage());
        }
    }
}
