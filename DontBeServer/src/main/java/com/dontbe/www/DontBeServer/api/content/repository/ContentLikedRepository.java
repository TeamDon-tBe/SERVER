package com.dontbe.www.DontBeServer.api.content.repository;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.content.domain.ContentLiked;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentLikedRepository extends JpaRepository<ContentLiked,Long> {
    boolean existsByContentAndMember(Content content, Member member);

    int countByContent(Content content);
}
