package com.dontbe.www.DontBeServer.external.slack.service;

import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.report.dto.ReportSlackRequestDto;
import com.dontbe.www.DontBeServer.common.util.TextUtil;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SlackService {
    private final MemberRepository memberRepository;
    public SlackService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Value(value = "${slack.token}")
    String slackToken;

    public void sendSlackMessage(Long totalMember, String channel) {
        String message = totalMember.toString() + "번째 예스비가 새롭게 탄생했어요.";
        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message)
                    .build();
            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendReportSlackMessage(Long triggerMemberId, ReportSlackRequestDto reportSlackRequestDto, String channel) {
        String triggerMemberNickname = memberRepository.findMemberByIdOrThrow(triggerMemberId).getNickname();
        String relateText = TextUtil.cuttingText(30, reportSlackRequestDto.relateText());
        String message = triggerMemberNickname + "님이" + reportSlackRequestDto.reportTargetNickname() + "님을 신고했습니다."
                + "\n" + "관련 내용 : " + relateText;
        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message)
                    .build();
            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}