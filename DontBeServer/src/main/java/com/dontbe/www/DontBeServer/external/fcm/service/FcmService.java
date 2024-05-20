package com.dontbe.www.DontBeServer.external.fcm.service;

import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.external.fcm.dto.FcmMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void initialize() throws IOException {
//        만약 로컬에서 돌린다면 json파일을 resources디렉토리 밑에 넣고 아래 EC2표시 된 부분을 각주, 아래를 각주 해제해 주세요.
        //local버전
//        ClassPathResource resource = new ClassPathResource("fire-base.json");
//        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(credentials)
//                .build();

        //ec2버전(prod or dev)
        String fireBaseJsonPath = "/home/ubuntu/fcm/fire-base.json";
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(fireBaseJsonPath)))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public void sendMessage(FcmMessageDto fcmMessageDto) {
        Message message = Message.builder()
                .setToken(fcmMessageDto.getMessage().getToken())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(fcmMessageDto.getMessage().getNotificationDetails().getTitle())
                        .setBody(fcmMessageDto.getMessage().getNotificationDetails().getBody())
                        .build()
                )
                .putAllData(objectMapper.convertValue(fcmMessageDto.getMessage().getData(), Map.class))
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}