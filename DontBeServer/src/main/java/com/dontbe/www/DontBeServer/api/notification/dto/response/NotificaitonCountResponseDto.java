package com.dontbe.www.DontBeServer.api.notification.dto.response;

public record NotificaitonCountResponseDto(
        int notificationNumber
) {
    public static NotificaitonCountResponseDto of(int number) {
        return new NotificaitonCountResponseDto(
                number
        );
    }
}
