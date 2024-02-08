package com.dontbe.www.DontBeServer.api.content.repository;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.common.exception.NotFoundException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findContentById(Long contentId);

    //게시물 전체 조회 관련

    List<Content> findAllByOrderByCreatedAtDesc();

    List<Content> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);

    //Slice<Content> findContentsTop30(PageRequest pageRequest);

    @Query("SELECT c FROM Content c WHERE c.id > :lastContentId ORDER BY c.createdAt")
    Slice<Content> findContentsNextPage(Long lastContentId, PageRequest pageRequest);

    //Slice<Content> findContentsTop30ByIdOrderByCreatedAtAsc(PageRequest pageRequest);

//    @Query("SELECT c FROM Content c WHERE c.id <= ?1 AND c.content.id = ?2 ORDER BY c.createdAt")
//    Slice<Content> findContentsByIdOrderByCreatedAtDesc(Long lastContentId, PageRequest pageRequest);

    default Content findContentByIdOrThrow(Long contentId) {
        return findContentById(contentId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_CONTENT.getMessage()));
    }
}