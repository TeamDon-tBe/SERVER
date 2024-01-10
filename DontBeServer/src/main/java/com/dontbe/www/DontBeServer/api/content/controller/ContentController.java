package com.dontbe.www.DontBeServer.api.content.controller;

import com.dontbe.www.DontBeServer.api.content.dto.request.ContentPostRequestDto;
import com.dontbe.www.DontBeServer.api.content.service.ContentCommandService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ContentController {
    private final ContentCommandService contentCommandService;

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

}
