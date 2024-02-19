package com.dontbe.www.DontBeServer.api.comment.repository;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.comment.domain.CommentLiked;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikedRepository extends JpaRepository<CommentLiked, Long> {
    boolean existsByCommentAndMember(Comment comment, Member member);

    int countByComment(Comment comment);

    void deleteByCommentAndMember(Comment comment, Member member);

    void deleteByComment(Comment comment);

    List<CommentLiked> findAllByMemberId(Long memberId);
}
