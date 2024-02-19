package com.dontbe.www.DontBeServer.api.ghost.service;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.ghost.dto.request.GhostClickRequestDto;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberClickGhostRequestDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class GhostCommandService {

    private final MemberRepository memberRepository;
    private final GhostRepository ghostRepository;
    private final NotificationRepository notificationRepository;
    public void clickMemberGhost(Long memberId, MemberClickGhostRequestDto memberClickGhostRequestDto) {
        Member triggerMember = memberRepository.findMemberByIdOrThrow(memberId);
        Member targetMember = memberRepository.findMemberByIdOrThrow(memberClickGhostRequestDto.targetMemberId());

        mySelfGhostBlock(triggerMember,targetMember);
        isDuplicateMemberGhost(triggerMember,targetMember);

        Ghost ghost = Ghost.builder()
                .ghostTargetMember(targetMember)
                .ghostTriggerMember(triggerMember)
                .build();
        Ghost savedGhost = ghostRepository.save(ghost);

        Notification notification = Notification.builder()
                .notificationTargetMember(targetMember)
                .notificationTriggerMemberId(memberId)
                .notificationTriggerType(memberClickGhostRequestDto.alarmTriggerType())
                .notificationTriggerId(memberClickGhostRequestDto.alarmTriggerId()) //2차 스프린트 : 에러수정을 위한 notificationTriggerId에 답글id 저장, 알림 조회시 답글id로 게시글id 반환하도록하기(refineNotificationTriggerId에 추가)
                .isNotificationChecked(false)
                .notificationText("")
                .build();
        Notification savedNotification = notificationRepository.save(notification);

        targetMember.decreaseGhost();

        if(targetMember.getMemberGhost() == -85) {
            Notification ghostNotification = Notification.builder()
                    .notificationTargetMember(targetMember)
                    .notificationTriggerMemberId(memberId)
                    .notificationTriggerType("beGhost")
                    .notificationTriggerId(memberClickGhostRequestDto.alarmTriggerId())
                    .isNotificationChecked(false)
                    .notificationText("")
                    .build();
            Notification savedGhostNotification = notificationRepository.save(ghostNotification);
        }
    }

    public void clickMemberGhost2(Long memberId, GhostClickRequestDto ghostClickRequestDto) {
        Member triggerMember = memberRepository.findMemberByIdOrThrow(memberId);
        Member targetMember = memberRepository.findMemberByIdOrThrow(ghostClickRequestDto.targetMemberId());

        mySelfGhostBlock(triggerMember,targetMember);
        isDuplicateMemberGhost(triggerMember,targetMember);

        Ghost ghost = Ghost.builder()
                .ghostTargetMember(targetMember)
                .ghostTriggerMember(triggerMember)
                .ghostReason(ghostClickRequestDto.ghostReason())
                .build();
        Ghost savedGhost = ghostRepository.save(ghost);

        Notification notification = Notification.builder()
                .notificationTargetMember(targetMember)
                .notificationTriggerMemberId(memberId)
                .notificationTriggerType(ghostClickRequestDto.alarmTriggerType())
                .notificationTriggerId(ghostClickRequestDto.alarmTriggerId()) //2차 스프린트 : 에러수정을 위한 notificationTriggerId에 답글id 저장, 알림 조회시 답글id로 게시글id 반환하도록하기(refineNotificationTriggerId에 추가)
                .isNotificationChecked(false)
                .notificationText("")
                .build();
        Notification savedNotification = notificationRepository.save(notification);

        targetMember.decreaseGhost();

        if(targetMember.getMemberGhost() == -85) {
            Notification ghostNotification = Notification.builder()
                    .notificationTargetMember(targetMember)
                    .notificationTriggerMemberId(memberId)
                    .notificationTriggerType("beGhost")
                    .notificationTriggerId(ghostClickRequestDto.alarmTriggerId())
                    .isNotificationChecked(false)
                    .notificationText("")
                    .build();
            Notification savedGhostNotification = notificationRepository.save(ghostNotification);
        }
    }

    private void isDuplicateMemberGhost(Member triggerMember, Member targetMember) {
        if(ghostRepository.existsByGhostTargetMemberAndGhostTriggerMember(targetMember,triggerMember)) {
            throw new BadRequestException(ErrorStatus.DUPLICATION_MEMBER_GHOST.getMessage());
        }
    }

    private void mySelfGhostBlock(Member triggerMember, Member targetMember) {
        if(Objects.equals(triggerMember.getId(), targetMember.getId())) {
            throw new BadRequestException(ErrorStatus.GHOST_MYSELF_BLOCK.getMessage());
        }
    }
}
