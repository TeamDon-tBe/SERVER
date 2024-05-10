package com.dontbe.www.DontBeServer.api.comment.controller;

import com.dontbe.www.DontBeServer.api.comment.dto.request.CommentLikedRequestDto;
import com.dontbe.www.DontBeServer.api.comment.dto.request.CommentPostRequestDto;
import com.dontbe.www.DontBeServer.api.comment.service.CommentCommendService;
import com.dontbe.www.DontBeServer.api.comment.service.CommentQueryService;
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
import org.springframework.web.multipart.MultipartFile;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Auth")
@Tag(name="답글 관련",description = "Comment Api Document")
public class CommentController {
    private final CommentCommendService commentCommendService;
    private final CommentQueryService commentQueryService;

    @PostMapping("v1/content/{contentId}/comment")
    @Operation(summary = "답글 작성 API입니다.", description = "CommentPost")
    public ResponseEntity<ApiResponse<Object>> postComment(Principal principal, @PathVariable Long contentId, @Valid @RequestBody CommentPostRequestDto commentPostRequestDto) {
        commentCommendService.postComment(MemberUtil.getMemberId(principal),contentId, commentPostRequestDto);
        return ApiResponse.success(POST_COMMENT_SUCCESS);
    }

    @PostMapping("v2/content/{contentId}/comment")
    @Operation(summary = "사진 첨부 기능이 추가된 답글 작성 API입니다.", description = "Comment Post +CommentImage")
    public ResponseEntity<ApiResponse<Object>> postComment2(Principal principal, @PathVariable Long contentId,
                                                            @RequestPart(value = "image", required = false) MultipartFile commentImage,
                                                            @Valid @RequestPart(value="text") CommentPostRequestDto commentPostRequestDto) {
        commentCommendService.postCommentVer2(MemberUtil.getMemberId(principal),contentId, commentImage, commentPostRequestDto);
        return ApiResponse.success(POST_COMMENT_SUCCESS);
    }

    @DeleteMapping("v1/comment/{commentId}")
    @Operation(summary = "답글 삭제 API입니다.", description = "CommentDelete")
    public ResponseEntity<ApiResponse<Object>> deleteComment(Principal principal, @PathVariable Long commentId){    //작성자ID와 댓글ID가 같아야 함
        commentCommendService.deleteComment(MemberUtil.getMemberId(principal),commentId);
        return ApiResponse.success(DELETE_COMMENT_SUCCESS);
    }

    @PostMapping("v1/comment/{commentId}/liked")
    @Operation(summary = "답글 좋아요 API입니다.", description = "CommentLiked")
    public ResponseEntity<ApiResponse<Object>> likeComment(Principal principal, @PathVariable Long commentId, @Valid @RequestBody CommentLikedRequestDto commentLikedRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        commentCommendService.likeComment(memberId, commentId, commentLikedRequestDto);
        return ApiResponse.success(COMMENT_LIKE_SUCCESS);
    }

    @DeleteMapping("v1/comment/{commentId}/unliked")
    @Operation(summary = "답글 좋아요 취소 API 입니다.", description = "CommentUnlike")
    public ResponseEntity<ApiResponse<Object>> unlikeComment(Principal principal,@PathVariable Long commentId) {
        Long memberId = MemberUtil.getMemberId(principal);
        commentCommendService.unlikeComment(memberId, commentId);
        return ApiResponse.success(COMMENT_UNLIKE_SUCCESS);
    }

    @GetMapping("v1/content/{contentId}/comment/all")  //페이지네이션 적용 후 지우기
    @Operation(summary = "게시물에 해당하는 답글 리스트 조회 API 입니다.", description = "CommentByContent")
    public ResponseEntity<ApiResponse<Object>> getCommentAll(Principal principal, @PathVariable Long contentId){
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_COMMENT_ALL_SUCCESS, commentQueryService.getCommentAll(memberId, contentId));
    }

    @GetMapping("v1/member/{memberId}/comments")   //페이지네이션 적용 후 지우기
    @Operation(summary = "멤버에 해당하는 답글 리스트 조회 API 입니다.", description = "CommentByMember")
    public ResponseEntity<ApiResponse<Object>> getMemberComment(Principal principal, @PathVariable Long memberId){
        Long usingMemberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_COMMENT_SECCESS, commentQueryService.getMemberComment(usingMemberId,memberId));
    }

    @Operation(summary = "페이지네이션이 적용된 게시물에 해당하는 답글 리스트 조회 API 입니다.", description = "CommentByContentPagination")
    @GetMapping("v1/content/{contentId}/comments")
    public ResponseEntity<ApiResponse<Object>> getCommentAllPagination(Principal principal, @PathVariable Long contentId, @RequestParam(value = "cursor") Long cursor){    //cursor= last commentId
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_COMMENT_ALL_SUCCESS, commentQueryService.getCommentAllPagination(memberId, contentId, cursor));
    }

    @Operation(summary = "페이지네이션이 적용된 게시물에 해당하는 답글 리스트 조회 API 입니다.", description = "CommentByContentPagination")
    @GetMapping("v2/content/{contentId}/comments")
    public ResponseEntity<ApiResponse<Object>> getCommentAllWithImage(Principal principal, @PathVariable Long contentId, @RequestParam(value = "cursor") Long cursor){    //cursor= last commentId
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_COMMENT_ALL_SUCCESS, commentQueryService.getCommentAllWithImage(memberId, contentId, cursor));
    }

    @Operation(summary = "페이지네이션이 적용된 멤버에 해당하는 답글 리스트 조회 API 입니다.", description = "CommentByMemberPagination")
    @GetMapping("v1/member/{memberId}/member-comments")
    public ResponseEntity<ApiResponse<Object>> getMemberCommentPagination(Principal principal, @PathVariable Long memberId, @RequestParam(value = "cursor") Long cursor){
        Long usingMemberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_COMMENT_SECCESS, commentQueryService.getMemberCommentPagination(usingMemberId,memberId,cursor));
    }
}