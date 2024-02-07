package com.dontbe.www.DontBeServer.common.util;


import static com.slack.api.model.block.composition.BlockCompositions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.slack.api.Slack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.webhook.WebhookPayloads;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackUtil {

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private final static String NEW_LINE = "\n";
    private final static String DOUBLE_NEW_LINE = "\n\n";
    private final Environment env;

    private StringBuilder sb = new StringBuilder();

    // Slack으로 알림 보내기
    public void sendAlert(Exception error, HttpServletRequest request) throws IOException {
        final String ENV_ACTIVE = env.getActiveProfiles()[0];

        // 현재 프로파일이 특정 프로파일이 아니면 알림보내지 않기
//        if (!env.getActiveProfiles()[0].equals("set1")) {
//            return;
//        }

        // 메시지 내용인 LayoutBlock List 생성
        List<LayoutBlock> layoutBlocks = generateLayoutBlock(error, request);

        // 슬랙의 send API과 webhookURL을 통해 생성한 메시지 내용 전송
        Slack.getInstance().send(webhookUrl, WebhookPayloads
                .payload(p ->
                        // 메시지 전송 유저명
                        p.username("Exception is detected 🚨")
                                // 메시지 전송 유저 아이콘 이미지 URL
                                .iconUrl(
                                        "https://user-images.githubusercontent.com/64000241/252282249-08381e4c-15b5-42b9-8ffc-1e887e688f16.png")
                                // 메시지 내용
                                .blocks(layoutBlocks)));
    }

    // 전체 메시지가 담긴 LayoutBlock 생성
    private List<LayoutBlock> generateLayoutBlock(Exception error, HttpServletRequest request) {
        return Blocks.asBlocks(
                getHeader("서버 측 오류로 예상되는 예외 상황이 발생하였습니다."),
                Blocks.divider(),
                getSection(generateErrorMessage(error)),
                Blocks.divider(),
                getSection(generateErrorPointMessage(request)),
                Blocks.divider(),
                // 이슈 생성을 위해 프로젝트의 Issue URL을 입력하여 바로가기 링크를 생성
                getSection("<https://github.com/TeamDon-tBe/SERVER/issues|이슈 생성하러 가기>")
        );
    }

    // 예외 정보 메시지 생성
    private String generateErrorMessage(Exception error) {
        sb.setLength(0);
        sb.append("*[🔥 Exception]*" + NEW_LINE).append(error.toString()).append(DOUBLE_NEW_LINE);
        sb.append("*[📩 From]*" + NEW_LINE).append(readRootStackTrace(error)).append(DOUBLE_NEW_LINE);

        return sb.toString();
    }

    // HttpServletRequest를 사용하여 예외발생 요청에 대한 정보 메시지 생성
    private String generateErrorPointMessage(HttpServletRequest request) {
        sb.setLength(0);
        sb.append("*[🧾세부정보]*" + NEW_LINE);
        sb.append("Request URL : " + request.getRequestURL().toString() + NEW_LINE);
        sb.append("Request Method : " + request.getMethod() + NEW_LINE);
        sb.append("Request Time : " + new Date() + NEW_LINE);

        // requestBody 가져오기
        String requestBody = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            requestBody = stringBuilder.toString();
        } catch (IOException e) {
            // requestBody를 읽는 도중 에러가 발생했을 때 처리
            log.error("Failed to read request body: {}", e.getMessage());
        }
        if (requestBody != null && !requestBody.isEmpty()) {
            sb.append("Request Body: " + requestBody + NEW_LINE);
        }

        // header 값 가져오기
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        sb.append("Headers: " + headers.toString() + NEW_LINE);

        return sb.toString();
    }

    // 예외발생 클래스 정보 return
    private String readRootStackTrace(Exception error) {
        return error.getStackTrace()[0].toString();
    }

    // 에러 로그 메시지의 제목 return
    private LayoutBlock getHeader(String text) {
        return Blocks.header(h -> h.text(
                plainText(pt -> pt.emoji(true)
                        .text(text))));
    }

    // 에러 로그 메시지 내용 return
    private LayoutBlock getSection(String message) {
        return Blocks.section(s ->
                s.text(BlockCompositions.markdownText(message)));
    }
}
