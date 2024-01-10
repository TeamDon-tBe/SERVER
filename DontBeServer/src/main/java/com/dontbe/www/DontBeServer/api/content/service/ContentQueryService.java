package com.dontbe.www.DontBeServer.api.content.service;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.dto.request.ContentPostRequestDto;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentQueryService {
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    public void postContent(Long memberId, ContentPostRequestDto contentPostRequestDto){
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Content content = Content.builder()
                .member(member)
                .contentText(contentPostRequestDto.contentText())
                .build();
        Content savedContent = contentRepository.save(content);
    }
}
