package com.dontbe.www.DontBeServer.api.content.controller;

import com.dontbe.www.DontBeServer.api.content.dto.request.*;
import com.dontbe.www.DontBeServer.api.content.dto.response.*;
import com.dontbe.www.DontBeServer.api.content.service.ContentCommandService;
import com.dontbe.www.DontBeServer.api.content.service.ContentQueryService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("api/")
@SecurityRequirement(name = "JWT Auth")
@Tag(name="게시글 관련", description = "Content API Document")
public class ContentController {
    private final ContentCommandService contentCommandService;
    private final ContentQueryService contentQueryService;

    @PostMapping("v1/content")
    @Operation(summary = "게시글 작성 API 입니다.",description = "Content Post")
    public ResponseEntity<ApiResponse<Object>> postContent(Principal principal, @Valid @RequestBody ContentPostRequestDto contentPostRequestDto) {
        contentCommandService.postContent(MemberUtil.getMemberId(principal),contentPostRequestDto);
        return ApiResponse.success(POST_CONTENT_SUCCESS);
    }

    @DeleteMapping("v1/content/{contentId}")
    @Operation(summary = "게시글 삭제 API 입니다.",description = "Content Delete")
    public ResponseEntity<ApiResponse<Object>> deleteContent(Principal principal, @PathVariable("contentId") Long contentId) {
        contentCommandService.deleteContent(MemberUtil.getMemberId(principal),contentId);
        return ApiResponse.success(DELETE_CONTENT_SUCCESS);
    }

    @GetMapping("v1/content/{contentId}/detail")
    @Operation(summary = "게시글 상세 조회 API 입니다.",description = "Content Get Detail")
    public ResponseEntity<ApiResponse<ContentGetDetailsResponseDto>> getContentDetail(Principal principal, @PathVariable("contentId") Long contentId) {
        return ApiResponse.success(GET_CONTENT_DETAIL_SUCCESS, contentQueryService.getContentDetail(MemberUtil.getMemberId(principal), contentId));
    }

    @GetMapping("v2/content/{contentId}/detail")
    @Operation(summary = "isDeleted가 추가된 게시글 상세 조회 API 입니다.",description = "Content Get Detail")
    public ResponseEntity<ApiResponse<ContentGetDetailsResponseDtoVer2>> getContentDetailVer2(Principal principal, @PathVariable("contentId") Long contentId) {
        return ApiResponse.success(GET_CONTENT_DETAIL_SUCCESS, contentQueryService.getContentDetailVer2(MemberUtil.getMemberId(principal), contentId));
    }

    @PostMapping("v1/content/{contentId}/liked")
    @Operation(summary = "게시글 좋아요 API 입니다.",description = "Content Like")
    public ResponseEntity<ApiResponse<Object>> likeContent(Principal principal, @PathVariable("contentId") Long contentId, @Valid @RequestBody ContentLikedRequestDto contentLikedRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        contentCommandService.likeContent(memberId, contentId,contentLikedRequestDto);
        return ApiResponse.success(CONTENT_LIKE_SUCCESS);
    }

    @DeleteMapping("v1/content/{contentId}/unliked")
    @Operation(summary = "게시글 좋아요 취소 API 입니다.",description = "Content Unlike")
    public ResponseEntity<ApiResponse<Object>> unlikeContent(Principal principal, @PathVariable("contentId") Long contentId) {
        Long memberId = MemberUtil.getMemberId(principal);
        contentCommandService.unlikeContent(memberId, contentId);
        return ApiResponse.success(CONTENT_UNLIKE_SUCCESS);
    }

    @GetMapping("v1/content/all")   //페이지네이션 적용 후 지우기
    @Operation(summary = "게시글 전체 조회 API 입니다.",description = "ContentGetAll")
    public ResponseEntity<ApiResponse<List<ContentGetAllResponseDto>>> getContentAll(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_CONTENT_ALL_SUCCESS, contentQueryService.getContentAll(memberId));
    }

    @GetMapping("v1/member/{memberId}/contents")   //페이지네이션 적용 후 지우기
    @Operation(summary = "멤버에 해당하는 게시글 조회 API 입니다.",description = "ContentGetAllByMember")
    public ResponseEntity<ApiResponse<List<ContentGetAllByMemberResponseDto>>> getContentAllByMember(Principal principal, @PathVariable("memberId") Long targetMemberId) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_CONTENT_ALL_SUCCESS, contentQueryService.getContentAllByMember(memberId, targetMemberId));
    }

    @GetMapping("v1/contents")
    @Operation(summary = "페이지네이션이 적용된 게시글 전체 조회 API 입니다.",description = "ContentGetPagination")
    public ResponseEntity<ApiResponse<List<ContentGetAllResponseDtoVer2>>> getContentAllPagination(Principal principal, @RequestParam(value = "cursor") Long cursor) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_CONTENT_ALL_SUCCESS, contentQueryService.getContentAllPagination(memberId, cursor));
    }

    @GetMapping("v1/member/{memberId}/member-contents")
    @Operation(summary = "페이지네이션이 적용된 멤버에 해당하는 게시글 리스트 조회 API 입니다.",description = "ContentByMemberPagination")
    public ResponseEntity<ApiResponse<List<ContentGetAllByMemberResponseDto>>> getContentAllByMemberPagination(Principal principal,
                                                                                                     @PathVariable("memberId") Long targetMemberId,  @RequestParam(value = "cursor") Long cursor) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_CONTENT_SUCCESS, contentQueryService.getContentAllByMemberPagination(memberId, targetMemberId, cursor));
    }
}
