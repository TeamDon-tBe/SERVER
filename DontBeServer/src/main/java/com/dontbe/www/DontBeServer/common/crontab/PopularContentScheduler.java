package com.dontbe.www.DontBeServer.common.crontab;

import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.repository.ContentLikedRepository;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import com.dontbe.www.DontBeServer.external.fcm.dto.FcmMessageDto;
import com.dontbe.www.DontBeServer.external.fcm.service.FcmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class PopularContentScheduler {
    private final ContentRepository contentRepository;
    private final ContentLikedRepository contentLikedRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    public PopularContentScheduler(ContentRepository contentRepository, ContentLikedRepository contentLikedRepository,
                                   CommentRepository commentRepository, NotificationRepository notificationRepository, MemberRepository memberRepository, FcmService fcmService){
        this.contentRepository = contentRepository;
        this.contentLikedRepository = contentLikedRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
        this.fcmService = fcmService;
    }

    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void popularContentNotification() {
        LocalDateTime startOfYesterday = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfYesterday = startOfYesterday.plusDays(1).minusSeconds(1);

        List<Content> contents = contentRepository.findAllContentsBetweenDatesOrderedByAsc(startOfYesterday, endOfYesterday);

        Content topContent = contents.stream()
                .map(c -> new AbstractMap.SimpleEntry<>(c, contentLikedRepository.countByContent(c) + commentRepository.countByContent(c) * 1.6))
                .filter(e -> e.getValue() >= 5)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        if (topContent != null) {
            Member topContentWriter = topContent.getMember();

            Notification popularWriterNotification = Notification.builder()
                    .notificationTargetMember(topContentWriter)
                    .notificationTriggerMemberId(-1L)
                    .notificationTriggerType("popularWriter")
                    .notificationTriggerId(topContent.getId())
                    .isNotificationChecked(false)
                    .notificationText("")
                    .build();
            Notification savedPopularWriterNotification = notificationRepository.save(popularWriterNotification);

            if(topContentWriter.isPushAlarmAllowed()) {
                String FcmMessageTitle = topContentWriter.getNickname() + "님이 작성하신 글이 인기들로 선정 되었어요.";

                FcmMessageDto commentFcmMessage = FcmMessageDto.builder()
                        .validateOnly(false)
                        .message(FcmMessageDto.Message.builder()
                                .notificationDetails(FcmMessageDto.NotificationDetails.builder()
                                        .title(FcmMessageTitle)
                                        .body("")
                                        .build())
                                .token(topContentWriter.getFcmToken())
                                .data(FcmMessageDto.Data.builder()
                                        .name("popularContent")
                                        .description("인기글 관련 푸시 알림")
                                        .relateContentId(topContent.getId())
                                        .build())
                                .build())
                        .build();

                fcmService.sendMessage(commentFcmMessage);
            }

            List<Member> activeMembers = memberRepository.findAllActiveMembers();

            for (Member activeMember : activeMembers) {
                Notification popularContentNotification = Notification.builder()
                        .notificationTargetMember(activeMember)
                        .notificationTriggerMemberId(-1L)
                        .notificationTriggerType("popularContent")
                        .notificationTriggerId(topContent.getId())
                        .isNotificationChecked(false)
                        .notificationText(topContent.getContentText())
                        .build();
                Notification savedPopularContentNotification = notificationRepository.save(popularContentNotification);
            }
        }
    }
}
