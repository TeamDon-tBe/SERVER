package com.dontbe.www.DontBeServer.api.ghost.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;

import java.time.LocalDate;
import java.util.List;

public interface GhostDbRepository extends CrudRepository<Ghost, Long> {

    // 다른 메소드들은 유지한 채로 findGhostsToUpdate 메소드를 추가
    @Query("SELECT g FROM Ghost g WHERE g.createdAt <= :fiveDaysAgo AND g.isRecovered = false")
    List<Ghost> findGhostsToUpdate(@Param("fiveDaysAgo") LocalDate fiveDaysAgo);

    @Modifying
    @Transactional
    @Query("UPDATE Ghost g SET g.isRecovered = true WHERE g.createdAt <= :fiveDaysAgo AND g.isRecovered = false")
    void updateGhostStatus(@Param("fiveDaysAgo") LocalDate fiveDaysAgo);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.memberGhost = m.memberGhost + 1 WHERE m = :member")
    void incrementMemberGhostCount(@Param("member") Member member);
}
