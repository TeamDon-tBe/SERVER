package com.dontbe.www.DontBeServer.api.member.dto.request;

public record MemberProfilePatchRequestDto (
        String nickname,
        boolean is_alarm_allowed,
        String member_intro,
        String profile_url
){
}
