package com.dontbe.www.DontBeServer.common.crontab;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostDbRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class GhostScheduler {

    private final GhostDbRepository ghostDbRepository;

    public GhostScheduler(GhostDbRepository ghostDbRepository) {
        this.ghostDbRepository = ghostDbRepository;
    }

    @Scheduled(cron = "0 20 20 * * ?") // 매일 00:00에 실행
    @Transactional
    public void updateGhostStatus() {
        LocalDate fiveDaysAgo = LocalDate.now().minusDays(5);
        // 5일 전 이전에 생성된 Ghost 엔터티 중 isRecovered가 false인 것들을 찾아 업데이트
        List<Ghost> ghostsToUpdate = ghostDbRepository.findGhostsToUpdate(fiveDaysAgo);
        for (Ghost ghost : ghostsToUpdate) {
            ghostDbRepository.updateGhostStatus(fiveDaysAgo);
            ghostDbRepository.incrementMemberGhostCount(ghost.getGhostTargetMember());
        }
    }
}
