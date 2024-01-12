package com.dontbe.www.DontBeServer.api.member.service.Impl;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetResponseDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberGetProfileResponseDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.member.service.MemberService;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

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
        return MemberGetProfileResponseDto.of(member);
    }
}
