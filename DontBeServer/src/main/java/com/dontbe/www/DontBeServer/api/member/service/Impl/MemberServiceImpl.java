package com.dontbe.www.DontBeServer.api.member.service.Impl;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberClickGhostRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberNicknameCheckRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetResponseDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberGetProfileResponseDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.member.service.MemberService;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.dontbe.www.DontBeServer.common.util.GhostUtil;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final GhostRepository ghostRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void withdrawalMember(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }

    @Override
    public MemberDetailGetResponseDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        String time = TimeUtilCustom.refineTimeMemberDetail(member.getCreatedAt());
        return MemberDetailGetResponseDto.of(member, time);
    }

    @Override
    public MemberGetProfileResponseDto getMemberProfile(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int memberGhost = GhostUtil.refineGhost(member.getMemberGhost());
        return MemberGetProfileResponseDto.of(member, memberGhost);
    }

    @Override
    @Transactional
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
                .notificationTriggerId(memberClickGhostRequestDto.alarmTriggerId())
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


    @Transactional
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
        if (memberProfilePatchRequestDto.is_alarm_allowed() | !memberProfilePatchRequestDto.is_alarm_allowed()) {
            existingMember.updateMemberIsAlarmAllowed(memberProfilePatchRequestDto.is_alarm_allowed());
        }

        // 저장
        Member savedMember = memberRepository.save(existingMember);
    }

    public void checkNicknameValidate(String nickname) {
        if(memberRepository.existsByNickname(nickname)){
            throw new BadRequestException(ErrorStatus.NICKNAME_VALIDATE_ERROR.getMessage());
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
