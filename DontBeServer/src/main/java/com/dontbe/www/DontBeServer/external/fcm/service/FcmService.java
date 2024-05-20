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
        try {
            ClassPathResource resource = new ClassPathResource("fire-base.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
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