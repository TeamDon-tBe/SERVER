package com.dontbe.www.DontBeServer.api.report.controller;

import com.dontbe.www.DontBeServer.api.report.dto.ReportSlackRequestDto;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import com.dontbe.www.DontBeServer.external.slack.service.SlackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.REPORT_SLACK_ALARM_SUCCESS;


@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Auth")
@Tag(name="신고 관련",description = "Report Api Document")
public class ReportController {
    private final SlackService slackService;

    @PostMapping("report/slack")
    @Operation(summary = "신고 시에 슬랙 알림 API입니다.",description = "ReportSlack")
    public ResponseEntity<ApiResponse<Object>> SlackReportMember(Principal principal, @RequestBody ReportSlackRequestDto reportSlackRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        slackService.sendReportSlackMessage(memberId, reportSlackRequestDto);
        return ApiResponse.success(REPORT_SLACK_ALARM_SUCCESS);
    }
}
