package com.dontbe.www.DontBeServer.api.comment.service;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.dto.request.CommentPostRequestDto;
import com.dontbe.www.DontBeServer.api.comment.repository.CommentRepository;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.member.repository.MemberRepository;
import com.dontbe.www.DontBeServer.common.exception.UnAuthorizedException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommendService {
    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;
    private final MemberRepository memberRepository;

    public void postComment(Long memberId, Long contentId, CommentPostRequestDto commentPostRequestDto){
        Content content = contentRepository.findContentByIdOrThrow(contentId); // 게시물id 잘못됐을 때 에러
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Comment comment = Comment.builder()
                .member(member)
                .content(content)
                .commentText(commentPostRequestDto.commentText())
                .build();

        Comment savedComment = commentRepository.save(comment);
    }

    public void deleteComment(Long memberId, Long commentId) {
        deleteValidate(memberId, commentId);
        commentRepository.deleteById(commentId);
    }

    public void deleteValidate(Long memberId, Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException(ErrorStatus.NOT_FOUND_COMMENT.getMessage()));

        Long commentMemberId = comment.getMember().getId();
        System.out.println(commentId);
        if (!commentMemberId.equals(memberId)) {    //답글작성자 != 현재 유저 >> 권한error
            throw new UnAuthorizedException(ErrorStatus.UNAUTHORIZED_MEMBER.getMessage());
        }
    }

}
