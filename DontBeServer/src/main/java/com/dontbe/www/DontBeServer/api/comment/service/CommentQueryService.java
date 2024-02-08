package com.dontbe.www.DontBeServer.api.comment.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.dto.response.CommentAllByMemberResponseDto;
import com.dontbe.www.DontBeServer.api.comment.dto.response.CommentAllResponseDto;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentLikedRepository;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
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
public class CommentQueryService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final GhostRepository ghostRepository;
    private final CommentLikedRepository commentLikedRepository;
    private final ContentRepository contentRepository;

    private final int COMMENT_DEFAULT_PAGE_SIZE = 15;

    public List<CommentAllResponseDto> getCommentAll(Long memberId, Long contentId) {
        contentRepository.findContentByIdOrThrow(contentId);

        List<Comment> commentList = commentRepository.findCommentsByContentIdOrderByCreatedAtAsc(contentId);

        return commentList.stream()
                .map( oneComment -> CommentAllResponseDto.of(
                        oneComment.getId(),
                        memberRepository.findMemberByIdOrThrow(oneComment.getMember().getId()),
                        checkGhost(memberId, oneComment.getId()),
                        checkMemberGhost(oneComment.getId()),
                        checkLikedComment(memberId,oneComment.getId()),
                        TimeUtilCustom.refineTime(oneComment.getCreatedAt()),
                        likedNumber(oneComment.getId()),
                        oneComment.getCommentText()))
                .collect(Collectors.toList());
    }

    public List<CommentAllByMemberResponseDto> getMemberComment(Long principalId, Long memberId){
        memberRepository.findMemberByIdOrThrow(memberId);
        List<Comment> commentList = commentRepository.findCommentsByMemberIdOrderByCreatedAtDesc(memberId);

        return commentList.stream()
                .map( oneComment -> CommentAllByMemberResponseDto.of(
                        memberRepository.findMemberByIdOrThrow(memberId),
                        checkLikedComment(principalId, oneComment.getId()),
                        checkGhost(principalId, oneComment.getId()),
                        checkMemberGhost(oneComment.getId()),
                        likedNumber(oneComment.getId()),
                        oneComment)
                ).collect(Collectors.toList());
    }

    public List<CommentAllResponseDto> getCommentAllPagination(Long memberId, Long contentId, Long cursor) {
        contentRepository.findContentByIdOrThrow(contentId);
        PageRequest pageRequest = PageRequest.of(0, COMMENT_DEFAULT_PAGE_SIZE);
        Slice<Comment> commentList;

        commentList = commentRepository.findCommentsByContentNextPage(cursor, contentId, pageRequest);

        return commentList.stream()
                .map(oneComment -> CommentAllResponseDto.of(
                        oneComment.getId(),
                        memberRepository.findMemberByIdOrThrow(oneComment.getMember().getId()),
                        checkGhost(memberId, oneComment.getId()),
                        checkMemberGhost(oneComment.getId()),
                        checkLikedComment(memberId,oneComment.getId()),
                        TimeUtilCustom.refineTime(oneComment.getCreatedAt()),
                        likedNumber(oneComment.getId()),
                        oneComment.getCommentText()))
                .collect(Collectors.toList());
    }

    public List<CommentAllByMemberResponseDto> getMemberCommentPagination(Long principalId, Long memberId, Long cursor) {
        memberRepository.findMemberByIdOrThrow(memberId);

        PageRequest pageRequest = PageRequest.of(0, COMMENT_DEFAULT_PAGE_SIZE);
        Slice<Comment> commentList;

        commentList = commentRepository.findCommentsByMemberNextPage(cursor, memberId, pageRequest);

        return commentList.stream()
                .map(oneComment -> CommentAllByMemberResponseDto.of(
                        memberRepository.findMemberByIdOrThrow(memberId),
                        checkLikedComment(principalId, oneComment.getId()),
                        checkGhost(principalId, oneComment.getId()),
                        checkMemberGhost(oneComment.getId()),
                        likedNumber(oneComment.getId()),
                        oneComment)
                ).collect(Collectors.toList());
    }

    private boolean checkGhost(Long usingMemberId, Long commentId) {
        Member writerMember = commentRepository.findCommentByIdOrThrow(commentId).getMember();
        return ghostRepository.existsByGhostTargetMemberIdAndGhostTriggerMemberId(writerMember.getId(), usingMemberId);
    }

    private boolean checkLikedComment(Long usingMemberId, Long commentId) {
        Member member = memberRepository.findMemberByIdOrThrow(usingMemberId);
        Comment comment = commentRepository.findCommentByIdOrThrow(commentId);
        return commentLikedRepository.existsByCommentAndMember(comment, member);
    }

    private int checkMemberGhost(Long commentId) {
        Member member = commentRepository.findCommentByIdOrThrow(commentId).getMember();
        return GhostUtil.refineGhost(member.getMemberGhost());
    }

    private int likedNumber(Long commentId) {
        Comment comment = commentRepository.findCommentByIdOrThrow(commentId);
        return commentLikedRepository.countByComment(comment);
    }
}
