package com.dontbe.www.DontBeServer.api.content.controller;

import com.dontbe.www.DontBeServer.api.content.dto.request.ContentPostRequestDto;
import com.dontbe.www.DontBeServer.api.content.repository.ContentRepository;
import com.dontbe.www.DontBeServer.api.content.service.ContentQueryService;
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
    private final ContentQueryService contentQueryService;

    @PostMapping("content")
    public ResponseEntity<ApiResponse<Object>> postContent(Principal principal, @Valid @RequestBody ContentPostRequestDto contentPostRequestDto) {
        contentQueryService.postContent(MemberUtil.getMemberId(principal),contentPostRequestDto);
        return ApiResponse.success(POST_CONTENT_SUCCESS);
    }

}
