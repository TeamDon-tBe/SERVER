package com.dontbe.www.DontBeServer.api.ghost.repository;

import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GhostRepository extends JpaRepository<Ghost, Long> {
    boolean existsByGhostTargetMemberIdAndGhostTriggerMemberId(Long ghostTargetMember, Long ghostTrigger);
}
