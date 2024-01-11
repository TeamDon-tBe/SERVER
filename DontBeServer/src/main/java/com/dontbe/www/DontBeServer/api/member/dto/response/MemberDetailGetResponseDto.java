package com.dontbe.www.DontBeServer.api.member.dto.response;

import com.dontbe.www.DontBeServer.api.member.domain.Member;

public record MemberDetailGetResponseDto(
        Long memberId,
        String joinDate,
        String showMemberId,
        String socialPlatform,
        String versionInformation
) {
    public static MemberDetailGetResponseDto of(Member member, String joinDate){
        return new MemberDetailGetResponseDto(
                member.getId(),
                joinDate,
                member.getSocialId(),
                member.getSocialPlatform().name() + " SOCIAL LOGIN",
                "1.0.01"
        );
    }
}
