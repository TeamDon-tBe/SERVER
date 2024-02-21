package com.dontbe.www.DontBeServer.api.comment.service;

import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class ExpiredCommentDeletedBatch {
    private final CommentRepository commentRepository;

    @Scheduled(cron = "0 0 0 * * ?")    //매일 밤 자정에 실행
    public void deleteExpiredComment() {
        commentRepository.deleteCommentScheduledForDeletion(LocalDateTime.now());
    }
}
