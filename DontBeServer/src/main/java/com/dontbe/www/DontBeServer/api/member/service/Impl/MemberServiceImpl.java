package com.dontbe.www.DontBeServer.api.member.service.Impl;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.api.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void withdrawalMember(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }
}
