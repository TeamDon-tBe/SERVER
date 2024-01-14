package com.dontbe.www.DontBeServer.api.notification.controller;

import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificaitonCountResponseDto;
import com.dontbe.www.DontBeServer.api.notification.service.NotificationCommandService;
import com.dontbe.www.DontBeServer.api.notification.service.NotificationQueryService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @PatchMapping("notification-check")
    public ResponseEntity<ApiResponse<Object>> readNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        notificationCommandService.readNotification(memberId);
        return ApiResponse.success(READ_NOTIFICATION_SUCCESS);
    }

    @GetMapping("notification/number")
    public ResponseEntity<ApiResponse<NotificaitonCountResponseDto>> countNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(COUNT_NOTIFICATION_SUCCESS, notificationQueryService.countUnreadNotification(memberId));
    }
}
