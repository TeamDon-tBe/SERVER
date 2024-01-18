package com.dontbe.www.DontBeServer.api.content.service;

import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.dto.response.ContentGetAllByMemberResponseDto;
import com.dontbe.www.DontBeServer.api.content.dto.response.ContentGetAllResponseDto;
import com.dontbe.www.DontBeServer.api.content.dto.response.ContentGetDetailsResponseDto;
import com.dontbe.www.DontBeServer.api.content.repository.ContentLikedRepository;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.common.util.GhostUtil;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentQueryService {
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;
    private final GhostRepository ghostRepository;
    private final ContentLikedRepository contentLikedRepository;

    //private final int DEFAULT_PAGE_SIZE = 20;

    public ContentGetDetailsResponseDto getContentDetail(Long memberId, Long contentId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Content content = contentRepository.findContentByIdOrThrow(contentId);
        Member writerMember = memberRepository.findMemberByIdOrThrow(content.getMember().getId());
        int writerMemberGhost = GhostUtil.refineGhost(writerMember.getMemberGhost());
        Long writerMemberId = content.getMember().getId();
        boolean isGhost = ghostRepository.existsByGhostTargetMemberIdAndGhostTriggerMemberId(writerMemberId, memberId);
        boolean isLiked = contentLikedRepository.existsByContentAndMember(content,member);
        String time = TimeUtilCustom.refineTime(content.getCreatedAt());
        int likedNumber = contentLikedRepository.countByContent(content);
        int commentNumber = commentRepository.countByContent(content);

        return ContentGetDetailsResponseDto.of(writerMember, writerMemberGhost, content, isGhost, isLiked, time, likedNumber, commentNumber);
    }

    public List<ContentGetAllResponseDto> getContentAll(Long memberId) {
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        List<Content> contents = contentRepository.findAllByOrderByCreatedAtDesc();
        return contents.stream()
                .map(content -> ContentGetAllResponseDto.of(content.getMember(), content,
                        ghostRepository.existsByGhostTargetMemberAndGhostTriggerMember(content.getMember(),usingMember),GhostUtil.refineGhost(content.getMember().getMemberGhost()),
                        contentLikedRepository.existsByContentAndMember(content,usingMember), TimeUtilCustom.refineTime(content.getCreatedAt()),
                        contentLikedRepository.countByContent(content), commentRepository.countByContent(content)))
                .collect(Collectors.toList());
    }

    /*
    public List<ContentGetAllResponseDto> getContentAll2(Long memberId, Long cursor) {
        PageRequest pageRequest = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        Slice<Content> contentList;

        if (cursor==-1) {
            contentList = contentRepository.findAllTop(pageRequest);
        } else {
            contentList = contentRepository.findcontentNextPage(cursor, pageRequest);
        }

        return contentList.stream()
                .map(content -> ContentGetAllResponseDto.of(content.getMember(), content,
                        ghostRepository.existsByGhostTargetMemberAndGhostTriggerMember(content.getMember(),usingMember),
                        contentLikedRepository.existsByContentAndMember(content,usingMember), TimeUtilCustom.refineTime(content.getCreatedAt()),
                        contentLikedRepository.countByContent(content), commentRepository.countByContent(content)))
                .collect(Collectors.toList());
    }*/
    public List<ContentGetAllByMemberResponseDto> getContentAllByMember(Long memberId, Long targetMemberId) {
    //public List<ContentGetAllByMemberResponseDto> getContentAllByMember(Long memberId, Long targetMemberId, Long cursor) {
        Member usingMember = memberRepository.findMemberByIdOrThrow(memberId);
        Member targetMember = memberRepository.findMemberByIdOrThrow(targetMemberId);
        List<Content> contents = contentRepository.findAllByMemberIdOrderByCreatedAtDesc(targetMemberId);
        return contents.stream()
                .map(content -> ContentGetAllByMemberResponseDto.of(targetMember, content,
                        ghostRepository.existsByGhostTargetMemberAndGhostTriggerMember(targetMember,usingMember),
                        contentLikedRepository.existsByContentAndMember(content,usingMember), TimeUtilCustom.refineTime(content.getCreatedAt()),
                        contentLikedRepository.countByContent(content), commentRepository.countByContent(content)))
                .collect(Collectors.toList());
    }
}
