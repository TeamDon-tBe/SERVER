package com.dontbe.www.DontBeServer.api.content.controller;

import com.dontbe.www.DontBeServer.api.content.dto.request.*;
import com.dontbe.www.DontBeServer.api.content.dto.response.*;
import com.dontbe.www.DontBeServer.api.content.service.ContentCommandService;
import com.dontbe.www.DontBeServer.api.content.service.ContentQueryService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ContentController {
    private final ContentCommandService contentCommandService;
    private final ContentQueryService contentQueryService;

    @PostMapping("content")
    public ResponseEntity<ApiResponse<Object>> postContent(Principal principal, @Valid @RequestBody ContentPostRequestDto contentPostRequestDto) {
        contentCommandService.postContent(MemberUtil.getMemberId(principal),contentPostRequestDto);
        return ApiResponse.success(POST_CONTENT_SUCCESS);
    }

    @DeleteMapping("content/{contentId}")
    public ResponseEntity<ApiResponse<Object>> deleteContent(Principal principal, @PathVariable("contentId") Long contentId) {
        contentCommandService.deleteContent(MemberUtil.getMemberId(principal),contentId);
        return ApiResponse.success(DELETE_CONTENT_SUCCESS);
    }

    @GetMapping("content/{contentId}/detail")
    public ResponseEntity<ApiResponse<ContentGetDetailsResponseDto>> getContentDetail(Principal principal, @PathVariable("contentId") Long contentId) {
        return ApiResponse.success(GET_CONTENT_DETAIL_SUCCESS, contentQueryService.getContentDetail(MemberUtil.getMemberId(principal), contentId));
    }

    @PostMapping("content/{contentId}/liked")
    public ResponseEntity<ApiResponse<Object>> likeContent(Principal principal, @PathVariable("contentId") Long contentId, @Valid @RequestBody ContentLikedRequestDto contentLikedRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        contentCommandService.likeContent(memberId, contentId,contentLikedRequestDto);
        return ApiResponse.success(CONTENT_LIKE_SUCCESS);
    }

    @DeleteMapping("content/{contentId}/unliked")
    public ResponseEntity<ApiResponse<Object>> unlikeContent(Principal principal, @PathVariable("contentId") Long contentId) {
        Long memberId = MemberUtil.getMemberId(principal);
        contentCommandService.unlikeContent(memberId, contentId);
        return ApiResponse.success(CONTENT_UNLIKE_SUCCESS);
    }

    @GetMapping("content/all")
    public ResponseEntity<ApiResponse<List<ContentGetAllResponseDto>>> getContentAll(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_CONTENT_ALL_SUCCESS, contentQueryService.getContentAll(memberId));
    }

    @GetMapping("member/{memberId}/contents")
    public ResponseEntity<ApiResponse<List<ContentGetAllByMemberResponseDto>>> getContentAllByMember(Principal principal, @PathVariable("memberId") Long targetMemberId) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_CONTENT_ALL_SUCCESS, contentQueryService.getContentAllByMember(memberId, targetMemberId));
    }
}
