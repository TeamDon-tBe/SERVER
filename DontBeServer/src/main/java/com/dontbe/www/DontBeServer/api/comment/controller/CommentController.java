package com.dontbe.www.DontBeServer.api.comment.controller;

import com.dontbe.www.DontBeServer.api.comment.dto.request.CommentPostRequestDto;
import com.dontbe.www.DontBeServer.api.comment.service.CommentCommendService;
import com.dontbe.www.DontBeServer.api.member.service.MemberService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.response.SuccessStatus;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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



}
