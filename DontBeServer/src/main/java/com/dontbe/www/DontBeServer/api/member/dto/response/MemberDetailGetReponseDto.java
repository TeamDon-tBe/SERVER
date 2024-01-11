package com.dontbe.www.DontBeServer.api.member.dto.response;

import com.dontbe.www.DontBeServer.api.member.domain.Member;

public record MemberDetailGetReponseDto(
        Long memberId,
        String joinDate,
        String showMemberId,
        String socialPlatform,
        String versionInformation
) {
    public static MemberDetailGetReponseDto of(Member member, String joinDate){
        return new MemberDetailGetReponseDto(
                member.getId(),
                joinDate,
                member.getSocialId(),
                member.getSocialPlatform().name() + " SOCIAL LOGIN",
                "1.0.01"
        );
    }
}
