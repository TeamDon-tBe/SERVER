package com.dontbe.www.DontBeServer.api.comment.repository;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByContent(Content content);

    Optional<Comment> findCommentById(Long commentId);

    Comment findCommentByMember(Member member);

    @Query("SELECT c FROM Comment c WHERE c.id <= ?1 AND c.content.id = ?2 ORDER BY c.createdAt DESC")
    Slice<Comment> findNextPage(Long lastCommentId, Long contentId, PageRequest pageRequest);

    //Slice<Comment> findTopById(int lastCommentId, Long contentId, PageRequest pageRequest);

    Slice<Comment> findCommentsTopByContentIdOrderByCreatedAtDesc(Long contentId, PageRequest pageRequest);

    List<Comment> findCommentsByMemberIdOrderByCreatedAtAsc(Long memberId);

    default Comment findCommentByIdOrThrow(Long commentId) {
        return findCommentById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_COMMENT.getMessage()));
    }
}
