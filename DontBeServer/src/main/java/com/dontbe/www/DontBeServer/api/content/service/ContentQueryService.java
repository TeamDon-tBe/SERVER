package com.dontbe.www.DontBeServer.api.content.service;

import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.dto.response.ContentGetDetailsResponseDto;
import com.dontbe.www.DontBeServer.api.content.repository.ContentLikedRepository;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentQueryService {
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;
    private final GhostRepository ghostRepository;
    private final ContentLikedRepository contentLikedRepository;

    public ContentGetDetailsResponseDto getContentDetail(Long memberId, Long contentId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Content content = contentRepository.findContentByIdOrThrow(contentId);
        Member writerMember = memberRepository.findMemberByIdOrThrow(content.getMember().getId());
        Long writerMemberId = content.getMember().getId();
        boolean isGhost = ghostRepository.existsByGhostTargetMemberIdAndGhostTriggerMemberId(writerMemberId, memberId);
        boolean isLiked = contentLikedRepository.existsByContentAndMember(content,member);
        LocalDateTime contentTime = content.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = contentTime.format(formatter);
        int likedNumber = contentLikedRepository.countByContent(content);
        int commentNumber = commentRepository.countByContent(content);

        return ContentGetDetailsResponseDto.of(writerMember, content, isGhost, isLiked, time, likedNumber, commentNumber);
    }
}
