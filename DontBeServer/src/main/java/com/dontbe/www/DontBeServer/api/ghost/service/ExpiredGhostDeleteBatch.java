package com.dontbe.www.DontBeServer.api.ghost.service;

import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class ExpiredGhostDeleteBatch {
    private final GhostRepository ghostRepository;

    @Scheduled(cron = "0 0 0 * * ?")    //매일 밤 자정에 실행
    public void deleteExpiredGhost() {
        ghostRepository.deleteGhostScheduledForDeletion(LocalDateTime.now());
    }
}
