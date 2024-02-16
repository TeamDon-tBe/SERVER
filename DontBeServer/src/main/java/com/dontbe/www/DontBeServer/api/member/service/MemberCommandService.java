package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final String DEFAULT_PROFILE_URL = "https://github.com/TeamDon-tBe/SERVER/assets/97835512/fb3ea04c-661e-4221-a837-854d66cdb77e";

    public void withdrawalMember(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);

        member.updateNickname("탈퇴한 회원");
        member.updateProfileUrl(DEFAULT_PROFILE_URL);

        notificationRepository.deleteBynotificationTargetMember(member);

        member.softDelete();
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
