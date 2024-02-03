package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetResponseDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberGetProfileResponseDto;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.dontbe.www.DontBeServer.common.util.GhostUtil;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public MemberDetailGetResponseDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        String time = TimeUtilCustom.refineTimeMemberDetail(member.getCreatedAt());
        return MemberDetailGetResponseDto.of(member, time);
    }

    public MemberGetProfileResponseDto getMemberProfile(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        int memberGhost = GhostUtil.refineGhost(member.getMemberGhost());
        return MemberGetProfileResponseDto.of(member, memberGhost);
    }

    public void checkNicknameValidate(String nickname) {
        if(memberRepository.existsByNickname(nickname)){
            throw new BadRequestException(ErrorStatus.NICKNAME_VALIDATE_ERROR.getMessage());
        }
    }
}
