package com.dontbe.www.DontBeServer.api.content.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ContentLikedRequestDto(
        @NotBlank String alarmTriggerType
) {
}
