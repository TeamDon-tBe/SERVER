package com.dontbe.www.DontBeServer.api.member.controller;

import com.dontbe.www.DontBeServer.api.member.service.MemberService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.response.SuccessStatus;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @DeleteMapping("/withdrawal")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse  withdrawalMember(Principal principal)
    {
        Long memberId = MemberUtil.getMemberId(principal);
        memberService.withdrawalMember(memberId);
        return ApiResponse.success(SuccessStatus.DRAWAL_SUCCESS.getStatusCode(), SuccessStatus.DRAWAL_SUCCESS.getMessage());
    }
}
