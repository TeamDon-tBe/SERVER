package com.dontbe.www.DontBeServer.api.notification.controller;

import com.dontbe.www.DontBeServer.api.notification.dto.response.NotificaitonCountResponseDto;
import com.dontbe.www.DontBeServer.api.notification.service.NotificationCommandService;
import com.dontbe.www.DontBeServer.api.notification.service.NotificationQueryService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Auth")
@Tag(name="노티 관련",description = "Notification Api Document")
public class NotificationController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @PatchMapping("notification-check")
    @Operation(summary = "노티 체크 API 입니다.",description = "NotificationCheck")
    public ResponseEntity<ApiResponse<Object>> readNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        notificationCommandService.readNotification(memberId);
        return ApiResponse.success(READ_NOTIFICATION_SUCCESS);
    }

    @GetMapping("notification/number")
    @Operation(summary = "노티 개수 반환 API 입니다.",description = "NotificationNumber")
    public ResponseEntity<ApiResponse<NotificaitonCountResponseDto>> countNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(COUNT_NOTIFICATION_SUCCESS, notificationQueryService.countUnreadNotification(memberId));
    }

    @GetMapping("/notification-all")
    @Operation(summary = "노티 전체 리스트 조회 API 입니다.",description = "NotificationGetAll")
    public ResponseEntity<ApiResponse<Object>> getNotification(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(NOTIFICATION_ALL_SUCCESS, notificationQueryService.getNotificationAll(memberId));
    }
}
