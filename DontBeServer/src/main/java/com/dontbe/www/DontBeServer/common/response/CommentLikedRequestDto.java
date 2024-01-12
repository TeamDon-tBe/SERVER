package com.dontbe.www.DontBeServer.common.response;

import jakarta.validation.constraints.NotBlank;

public record CommentLikedRequestDto(
        @NotBlank String notificationTriggerType,
        @NotBlank String notificationText
) {
}
