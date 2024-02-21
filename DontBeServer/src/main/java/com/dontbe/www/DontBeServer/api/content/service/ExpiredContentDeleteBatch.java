package com.dontbe.www.DontBeServer.api.content.service;


import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class ExpiredContentDeleteBatch {
    private final ContentRepository contentRepository;

    @Scheduled(cron = "0 0 0 * * ?")    //매일 밤 자정에 실행
    public void deleteExpiredContent() {
        contentRepository.deleteContentScheduledForDeletion(LocalDateTime.now());
    }
}
