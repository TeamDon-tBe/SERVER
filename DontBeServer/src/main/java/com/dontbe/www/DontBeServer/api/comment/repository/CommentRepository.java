package com.dontbe.www.DontBeServer.api.comment.repository;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByContent(Content content);

    List<Comment> findCommentsByContentId(Long contentId);

    Optional<Comment> findCommentById(Long commentId);

    List<Comment> findCommentsByMemberIdOrderByCreatedAtDesc(Long memberId);

    List<Comment> findCommentsByContentIdOrderByCreatedAtAsc(Long contentId);
    /*
    //게시물에 해당하는 답글 리스트 조회
    @Query("SELECT c FROM Comment c WHERE c.id <= ?1 AND c.content.id = ?2 ORDER BY c.createdAt Asc")
    Slice<Comment> findContentNextPage(Long lastCommentId, Long contentId, PageRequest pageRequest);

    Slice<Comment> findCommentsTopByContentIdOrderByCreatedAtDesc(Long contentId, PageRequest pageRequest);

    //멤버에 해당하는 답글 리스트 조회
    @Query("SELECT c FROM Comment c WHERE c.id <= ?1 AND c.member.id = ?2  ORDER BY c.createdAt DESC")
    Slice<Comment> findMemberNextPage(Long lastCommentId, Long memberId,PageRequest pageRequest);

    Slice<Comment> findCommentsTopByMemberIdOrderByCreatedAtDesc(Long memberId,PageRequest pageRequest);
    */

    default Comment findCommentByIdOrThrow(Long commentId) {
        return findCommentById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_COMMENT.getMessage()));
    }
}