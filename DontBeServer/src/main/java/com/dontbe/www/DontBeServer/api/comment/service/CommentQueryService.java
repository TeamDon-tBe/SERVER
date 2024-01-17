package com.dontbe.www.DontBeServer.api.comment.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.dto.response.CommentAllByMemberResponseDto;
import com.dontbe.www.DontBeServer.api.comment.dto.response.CommentAllResponseDto;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentLikedRepository;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
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

    private final int DEFAULT_PAGE_SIZE = 20;

    public List<CommentAllResponseDto> getCommentAll(Long memberId, Long contentId) {
        List<Comment> commentList = commentRepository.findCommentsByContentIdOrderByCreatedAtDesc(contentId);

        return commentList.stream()
                .map( oneComment -> CommentAllResponseDto.of(
                        memberRepository.findMemberByIdOrThrow(memberId),
                        checkGhost(memberId, oneComment.getId()),
                        checkMemberGhost(oneComment.getId()),
                        checkLikedComment(memberId,oneComment.getId()),
                        TimeUtilCustom.refineTime(oneComment.getCreatedAt()),
                        likedNumber(oneComment.getId()),
                        oneComment.getCommentText()))
                .collect(Collectors.toList());
    }

    public List<CommentAllByMemberResponseDto> getMemberComment(Long principalId, Long memberId){
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
    /*
    public List<CommentAllResponseDto> getCommentAll(Long memberId, Long contentId, Long cursor) {
        PageRequest pageRequest = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        Slice<Comment> commentList;

        if (cursor==-1) {
             commentList = commentRepository.findCommentsTopByContentIdOrderByCreatedAtDesc(contentId, pageRequest);
        } else {
            commentList = commentRepository.findContentNextPage(cursor, contentId, pageRequest);
        }

        return commentList.stream()
                .map(oneComment -> CommentAllResponseDto.of(
                        memberRepository.findMemberByIdOrThrow(memberId),
                        checkGhost(memberId, oneComment.getId()),
                        checkMemberGhost(oneComment.getId()),
                        checkLikedComment(memberId, oneComment.getId()),
                        TimeUtilCustom.refineTime(oneComment.getCreatedAt()),
                        likedNumber(contentId),
                        oneComment.getCommentText()))
                .collect(Collectors.toList());
    }

    public List<CommentAllByMemberResponseDto> getMemberComment(Long principalId, Long memberId, Long cursor) {
        PageRequest pageRequest = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        Slice<Comment> commentList;

        if (cursor==-1) {
            commentList = commentRepository.findCommentsTopByMemberIdOrderByCreatedAtDesc(memberId, pageRequest);
        } else {
            commentList = commentRepository.findMemberNextPage(cursor, memberId, pageRequest);
        }

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
    */

    private boolean checkGhost(Long usingMemberId, Long commentId) {
        Member writerMember = commentRepository.findCommentByIdOrThrow(commentId).getMember();
        return ghostRepository.existsByGhostTargetMemberIdAndGhostTriggerMemberId(usingMemberId, writerMember.getId());
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
