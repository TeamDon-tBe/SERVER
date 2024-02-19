package com.dontbe.www.DontBeServer.api.ghost.repository;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface GhostRepository extends JpaRepository<Ghost, Long> {
    boolean existsByGhostTargetMemberIdAndGhostTriggerMemberId(Long ghostTargetMember, Long ghostTrigger);

    boolean existsByGhostTargetMemberAndGhostTriggerMember(Member ghostTargetMember, Member ghostTriggerMember);

    List<Ghost>findByGhostTargetMember(Member member);

    List<Ghost>findByGhostTriggerMember(Member member);

    @Query("DELETE FROM Ghost g WHERE g.isDeleted = true AND g.deleteAt < :currentDate")
    void deleteGhostScheduledForDeletion(LocalDateTime currentDate);
}
