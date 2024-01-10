package com.dontbe.www.DontBeServer.api.member.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberById(Long memberId);

    default Member findMemberByIdOrThrow(Long memberId) {
        return findMemberById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER.getMessage()));
    }

    boolean existsBySocialId(String socialId);
    Optional<Member> findByIdAndRefreshToken(Long memberId, String refreshToken);

    Optional<Member> findBySocialId(String socialId);
}