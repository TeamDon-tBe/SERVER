package com.dontbe.www.DontBeServer.api.comment.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.dto.request.CommentLikedRequestDto;
import com.dontbe.www.DontBeServer.api.comment.dto.response.CommentAllResponseDto;
import com.dontbe.www.DontBeServer.api.comment.dto.response.ListCommentAllResponseDto;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentLikedRepository;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.repository.ContentLikedRepository;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.ghost.domain.Ghost;
import com.dontbe.www.DontBeServer.api.ghost.repository.GhostRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.common.util.GhostUtil;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;
import lombok.RequiredArgsConstructor;
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

    public List<CommentAllResponseDto> getCommentAll(Long memberId, Long contentId) {
//        Member principal = ;
//        Comment comment = commentRepository.findCommentByContentId(contentId);
//        Member writerMember = comment.getMember();
//        int writerMemberGhost = GhostUtil.refineGhost(writerMember.getMemberGhost());
//        boolean isGhost = ghostRepository.existsByGhostTargetMemberIdAndGhostTriggerMemberId(memberId, writerMember.getId());
//        boolean isLiked = commentLikedRepository.existsByCommentAndMember(comment,principal);
//        String time = TimeUtilCustom.refineTime(comment.getCreatedAt());
//        int likedNumber = commentLikedRepository.countByComment(comment);
        //oneComment.getMember()
          List<Comment> commentList = commentRepository.findAll();
        // int likedNumber = likedNumber(contentId);

        return commentList.stream()
                .map( oneComment -> CommentAllResponseDto.of(
                        memberRepository.findMemberByIdOrThrow(memberId),
                        checkGhost(memberId, oneComment.getId()),
                        checkMemberGhost(contentId),
                        checkLikedComment(memberId,oneComment.getId()),
                        TimeUtilCustom.refineTime(oneComment.getCreatedAt()),
                        likedNumber(contentId),
                        oneComment.getCommentText()))
                .collect(Collectors.toList());


    }
    private boolean checkGhost(Long usingMemberId, Long commentId){
        Member member = memberRepository.findMemberByIdOrThrow(usingMemberId);
        Member writerMember = commentRepository.findCommentByIdOrThrow(commentId).getMember();
        return ghostRepository.existsByGhostTargetMemberIdAndGhostTriggerMemberId(usingMemberId, writerMember.getId());
    }
    private boolean checkLikedComment(Long usingMemberId, Long commentId) {
        Member member = memberRepository.findMemberByIdOrThrow(usingMemberId);
        Comment comment = commentRepository.findCommentByIdOrThrow(commentId);
        return commentLikedRepository.existsByCommentAndMember(comment,member);
    }
    private int checkMemberGhost(Long contentId){
        Member member = contentRepository.findContentByIdOrThrow(contentId).getMember();
        return GhostUtil.refineGhost(member.getMemberGhost());
    }
    private int likedNumber(Long commentId){
        Comment comment = commentRepository.findCommentByIdOrThrow(commentId);
        return commentLikedRepository.countByComment(comment);
    }
}