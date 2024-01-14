package com.dontbe.www.DontBeServer.api.comment.repository;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByContent(Content content);

    Optional<Comment> findCommentById(Long commentId);

    Comment findCommentByMember(Member member);

    List<Comment> findCommentsByContentId(Long contentId);

    List<Comment> findCommentsByMemberId(Long memberId);

    default Comment findCommentByIdOrThrow(Long commentId) {
        return findCommentById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_COMMENT.getMessage()));
    }
}
