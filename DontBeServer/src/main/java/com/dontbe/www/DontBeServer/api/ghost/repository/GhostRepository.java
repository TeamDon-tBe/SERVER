package com.dontbe.www.DontBeServer.api.ghost.repository;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GhostRepository extends JpaRepository<Ghost, Long> {
    boolean existsByGhostTargetMemberIdAndGhostTriggerMemberId(Long ghostTargetMember, Long ghostTrigger);

    boolean existsByGhostTargetMemberAndGhostTriggerMember(Member ghostTargetMember, Member ghostTriggerMember);
}
