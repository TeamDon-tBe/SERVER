package com.dontbe.www.DontBeServer.api.ghost.dto.request;

import jakarta.validation.constraints.NotNull;

public record GhostClickRequestDto(
        String alarmTriggerType,
        Long targetMemberId,
        Long alarmTriggerId,
        @NotNull
        String ghostReason
) {
}
