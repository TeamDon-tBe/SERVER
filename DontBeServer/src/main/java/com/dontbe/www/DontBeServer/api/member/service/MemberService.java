package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.member.dto.request.MemberClickGhostRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetResponseDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberGetProfileResponseDto;

public interface MemberService {
    //유저 탈퇴하기
    void withdrawalMember(Long memberId);

    MemberDetailGetResponseDto getMemberDetail(Long memberId);

    MemberGetProfileResponseDto getMemberProfile(Long memberId);

    void clickMemberGhost(Long memberId, MemberClickGhostRequestDto memberClickGhostRequestDto);
}
