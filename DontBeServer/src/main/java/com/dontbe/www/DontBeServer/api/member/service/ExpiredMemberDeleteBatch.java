package com.dontbe.www.DontBeServer.api.member.service;

import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class ExpiredMemberDeleteBatch {
    private final MemberRepository memberRepository;

    //@Scheduled(cron="0/10 * * * * *")   //10초에 한번씩 실행
    @Scheduled(cron = "0 0 0 * * ?")    //매일 밤 자정에 실행
    public void deleteExpiredUser() {
        memberRepository.deleteMemberScheduledForDeletion(LocalDateTime.now());
        System.out.println("deleteExpiredUser");
    }
}
