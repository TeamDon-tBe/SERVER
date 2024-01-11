package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetReponseDto;

public interface MemberService {
    //유저 탈퇴하기
    void withdrawalMember(Long memberId);

    MemberDetailGetReponseDto getMemberDetail(Long memberId);
}
