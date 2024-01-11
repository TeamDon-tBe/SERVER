package com.dontbe.www.DontBeServer.api.comment.controller;

import com.dontbe.www.DontBeServer.api.comment.dto.request.CommentPostRequestDto;
import com.dontbe.www.DontBeServer.api.comment.service.CommentCommendService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.DELETE_COMMENT_SUCCESS;
import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.POST_COMMENT_SUCCESS;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class CommentController {
    private final CommentCommendService commentCommendService;

    @PostMapping("content/{contentId}/comment")
    public ResponseEntity<ApiResponse<Object>> postComment(Principal principal, @PathVariable Long contentId, @Valid @RequestBody CommentPostRequestDto commentPostRequestDto) {
        commentCommendService.postComment(MemberUtil.getMemberId(principal),contentId, commentPostRequestDto);
        return ApiResponse.success(POST_COMMENT_SUCCESS);
    }
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(Principal principal, @PathVariable Long commentId){    //작성자ID와 댓글ID가 같아야 함
        commentCommendService.deleteComment(MemberUtil.getMemberId(principal),commentId);
        return ApiResponse.success(DELETE_COMMENT_SUCCESS);
    }
}
