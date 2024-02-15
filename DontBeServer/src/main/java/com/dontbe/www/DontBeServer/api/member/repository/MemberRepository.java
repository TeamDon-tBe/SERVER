package com.dontbe.www.DontBeServer.api.member.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberById(Long memberId);
    Optional<Member> findMemberByRefreshToken(String refreshToken);

    default Member findMemberByIdOrThrow(Long memberId) {
        return findMemberById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER.getMessage()));
    }

    default Member findByRefreshTokenOrThrow(String refreshToken) {
        return findMemberByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER.getMessage()));
    }

    boolean existsBySocialId(String socialId);

    Optional<Member> findBySocialId(String socialId);

    boolean existsByNickname(String nickname);

    @Query("DELETE FROM Member m WHERE m.isDeleted = true AND m.deleteAt < :currentDate")
    void deleteMemberScheduledForDeletion(LocalDateTime currentDate);
}