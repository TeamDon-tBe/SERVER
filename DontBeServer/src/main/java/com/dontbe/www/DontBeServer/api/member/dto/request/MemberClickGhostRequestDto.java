package com.dontbe.www.DontBeServer.api.member.dto.request;

public record MemberClickGhostRequestDto(
        String alarmTriggerType,
        Long targetMemberId,
        Long alarmTriggerId
) {
}
