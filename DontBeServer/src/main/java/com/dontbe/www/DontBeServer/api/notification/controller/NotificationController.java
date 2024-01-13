package com.dontbe.www.DontBeServer.api.notification.controller;

import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificaitonCountResponseDto;
import com.dontbe.www.DontBeServer.api.notification.service.NotificationCommandService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationCommandService notificationCommandService;

    @PostMapping("notification-check")
    public ResponseEntity<ApiResponse<Object>> readNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        notificationCommandService.readNotification(memberId);
        return ApiResponse.success(READ_NOTIFICATION_SUCCESS);
    }

    @GetMapping("notification/number")
    public ResponseEntity<ApiResponse<NotificaitonCountResponseDto>> countNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(COUNT_NOTIFICATION_SUCCESS, notificationCommandService.countUnreadNotification(memberId));
    }
}
