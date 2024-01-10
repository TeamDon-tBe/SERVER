package com.dontbe.www.DontBeServer.api.comment.repository;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.content.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByContent(Content content);
}
