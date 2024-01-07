package com.dontbe.www.DontBeServer.api.member.repository;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    boolean existsBySocialId(String socialId);
    Optional<Member> findByIdAndRefreshToken(Long memberId, String refreshToken);

    Optional<Member> findBySocialId(String socialId);
}