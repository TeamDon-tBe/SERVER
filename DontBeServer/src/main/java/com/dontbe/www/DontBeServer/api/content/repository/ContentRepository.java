package com.dontbe.www.DontBeServer.api.content.repository;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content,Long> {
    Optional<Content> findContentById(Long contentId);

    default Content findContentByIdOrThrow(Long contentId) {
        return findContentById(contentId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_CONTENT.getMessage()));
    }

    List<Content> findAllByMemberId(Long memberId);
}