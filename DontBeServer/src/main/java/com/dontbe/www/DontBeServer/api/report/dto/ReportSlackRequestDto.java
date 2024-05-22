package com.dontbe.www.DontBeServer.api.report.dto;

public record ReportSlackRequestDto(
        String reportTargetNickname,
        String relateText
) {
}
