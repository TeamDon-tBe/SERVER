package com.dontbe.www.DontBeServer.api.content.controller;

import com.dontbe.www.DontBeServer.api.content.dto.request.*;
import com.dontbe.www.DontBeServer.api.content.dto.response.*;
import com.dontbe.www.DontBeServer.api.content.service.ContentCommandService;
import com.dontbe.www.DontBeServer.api.content.service.ContentQueryService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="게시글 관련", description = "Content API Document")
public class ContentController {
    private final ContentCommandService contentCommandService;
    private final ContentQueryService contentQueryService;

    @PostMapping("content")
    @Operation(summary = "게시글 작성 API 입니다.",description = "Content Post")
    public ResponseEntity<ApiResponse<Object>> postContent(Principal principal, @Valid @RequestBody ContentPostRequestDto contentPostRequestDto) {
        contentCommandService.postContent(MemberUtil.getMemberId(principal),contentPostRequestDto);
        return ApiResponse.success(POST_CONTENT_SUCCESS);
    }

    @DeleteMapping("content/{contentId}")
    @Operation(summary = "게시글 작성 API 입니다.",description = "Content Delete")
    public ResponseEntity<ApiResponse<Object>> deleteContent(Principal principal, @PathVariable("contentId") Long contentId) {
        contentCommandService.deleteContent(MemberUtil.getMemberId(principal),contentId);
        return ApiResponse.success(DELETE_CONTENT_SUCCESS);
    }

    @GetMapping("content/{contentId}/detail")
    @Operation(summary = "게시글 상세 조회 API 입니다.",description = "Content Get Detail")
    public ResponseEntity<ApiResponse<ContentGetDetailsResponseDto>> getContentDetail(Principal principal, @PathVariable("contentId") Long contentId) {
        return ApiResponse.success(GET_CONTENT_DETAIL_SUCCESS, contentQueryService.getContentDetail(MemberUtil.getMemberId(principal), contentId));
    }

    @PostMapping("content/{contentId}/liked")
    @Operation(summary = "게시글 좋아요 API 입니다.",description = "Content Like")
    public ResponseEntity<ApiResponse<Object>> likeContent(Principal principal, @PathVariable("contentId") Long contentId, @Valid @RequestBody ContentLikedRequestDto contentLikedRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        contentCommandService.likeContent(memberId, contentId,contentLikedRequestDto);
        return ApiResponse.success(CONTENT_LIKE_SUCCESS);
    }

    @DeleteMapping("content/{contentId}/unliked")
    @Operation(summary = "게시글 좋아요 취소 API 입니다.",description = "Content Unlike")
    public ResponseEntity<ApiResponse<Object>> unlikeContent(Principal principal, @PathVariable("contentId") Long contentId) {
        Long memberId = MemberUtil.getMemberId(principal);
        contentCommandService.unlikeContent(memberId, contentId);
        return ApiResponse.success(CONTENT_UNLIKE_SUCCESS);
    }

      @GetMapping("content/{contentId}/comment/all")
    public ResponseEntity<ApiResponse<Object>> getCommentAll(Principal principal, @PathVariable Long contentId){
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_COMMENT_ALL_SUCCESS, commentQueryService.getCommentAll(memberId, contentId));
    }
    @GetMapping("member/{memberId}/comments")
    public ResponseEntity<ApiResponse<Object>> getMemberComment(Principal principal, @PathVariable Long memberId){
        Long usingMemberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_COMMENT_SECCESS, commentQueryService.getMemberComment(usingMemberId,memberId));
    }
    /*
    @GetMapping("content/{contentId}/comments")
    public ResponseEntity<ApiResponse<Object>> getCommentAll(Principal principal, @PathVariable Long contentId, @RequestParam(value = "cursor") Long cursor){    //cursor= last commentId
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_COMMENT_ALL_SUCCESS, commentQueryService.getCommentAll(memberId, contentId, cursor));
    }
    @GetMapping("member/{memberId}/comments")
    public ResponseEntity<ApiResponse<Object>> getMemberComment(Principal principal, @PathVariable Long memberId, @RequestParam(value = "cursor") Long cursor){
        Long usingMemberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_COMMENT_SECCESS, commentQueryService.getMemberComment(usingMemberId,memberId,cursor));
    }
    */
}
