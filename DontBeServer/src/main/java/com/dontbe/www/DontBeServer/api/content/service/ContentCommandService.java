package com.dontbe.www.DontBeServer.api.content.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
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
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.dontbe.www.DontBeServer.common.util.GhostUtil;
import com.dontbe.www.DontBeServer.external.fcm.dto.FcmMessageDto;
import com.dontbe.www.DontBeServer.external.fcm.service.FcmService;
import com.dontbe.www.DontBeServer.external.s3.service.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentCommandService {
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentLikedRepository contentLikedRepository;
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;
    private final S3Service s3Service;
    private final FcmService fcmService;
    private static final String POST_IMAGE_FOLDER_NAME = "contents/";

    public void postContent(Long memberId, ContentPostRequestDto contentPostRequestDto) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);

        GhostUtil.isGhostMember(member.getMemberGhost());

        Content content = Content.builder()
                .member(member)
                .contentText(contentPostRequestDto.contentText())
                .build();
        Content savedContent = contentRepository.save(content);
    }

    public void postContentVer2(Long memberId, MultipartFile contentImage, ContentPostRequestDto contentPostRequestDto) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);

        GhostUtil.isGhostMember(member.getMemberGhost());

        Content content = contentRepository.save(Content.builder()
                .member(member)
                .contentText(contentPostRequestDto.contentText())
                .build());

        if(contentImage != null){   //이미지를 업로드 했다면
            try {
                final String contentImageUrl = s3Service.uploadImage2(POST_IMAGE_FOLDER_NAME+content.getId().toString()+"/", contentImage);
                content.setContentImage(contentImageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public void deleteContent(Long memberId, Long contentId) {
        deleteValidate(memberId, contentId);
        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);
        for(Comment comment : comments) {
            notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("commentLiked",comment.getId());
            notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("commentGhost",comment.getId());
            notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("comment", comment.getId());

            comment.softDelete();
        }
        notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("contentLiked",contentId);
        notificationRepository.deleteByNotificationTriggerTypeAndNotificationTriggerId("contentGhost",contentId);

        Content deleteContent = contentRepository.findContentByIdOrThrow(contentId);
        deleteContent.softDelete();
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
        if(triggerMember != targetMember){  //자신 게시물에 대한 좋아요 누르면 알림 발생 x
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

        if(targetMember.isPushAlarmAllowed()) {
            String FcmMessageTitle = triggerMember.getNickname() + "님이" + targetMember.getNickname() + "님의 글을 좋아합니다.";

            FcmMessageDto contentLikeFcmMessage = FcmMessageDto.builder()
                    .validateOnly(false)
                    .message(FcmMessageDto.Message.builder()
                            .notificationDetails(FcmMessageDto.NotificationDetails.builder()
                                    .title(FcmMessageTitle)
                                    .body("")
                                    .build())
                            .token(targetMember.getFcmToken())
                            .data(FcmMessageDto.Data.builder()
                                    .name("contentLike")
                                    .description("답글 좋아요 푸시 알림")
                                    .relateContentId(contentId)
                                    .build())
                            .build())
                    .build();

            fcmService.sendMessage(contentLikeFcmMessage);
        }
    }

    public void unlikeContent(Long memberId, Long contentId) {
        Member triggerMember = memberRepository.findMemberByIdOrThrow(memberId);
        Content content = contentRepository.findContentByIdOrThrow(contentId);

        if(!contentLikedRepository.existsByContentAndMember(content,triggerMember)){
            throw new BadRequestException(ErrorStatus.UNEXITST_CONTENT_LIKE.getMessage());
        }

        contentLikedRepository.deleteByContentAndMember(content,triggerMember);

        Member targetMember = contentRepository.findContentByIdOrThrow(contentId).getMember();

        notificationRepository.deleteByNotificationTargetMemberAndNotificationTriggerMemberIdAndNotificationTriggerTypeAndNotificationTriggerId(
                targetMember, memberId, "contentLiked", contentId);
    }

    private void deleteValidate(Long memberId, Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorStatus.NOT_FOUND_CONTENT.getMessage()));

        Long contentMemberId = content.getMember().getId();
        if (!contentMemberId.equals(memberId)) {
            throw new BadRequestException (ErrorStatus.UNAUTHORIZED_MEMBER.getMessage());
        }
    }

    private void isDuplicateContentLike(Content content, Member member) {
        if(contentLikedRepository.existsByContentAndMember(content,member)) {
            throw new BadRequestException(ErrorStatus.DUPLICATION_CONTENT_LIKE.getMessage());
        }
    }
}
