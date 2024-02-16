package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
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

    public void withdrawalMember(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }

    public void testWithdrawalMember(Long memberId){
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        List<Content> contentList = contentRepository.findContentByMember(member);
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
