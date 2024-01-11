package com.dontbe.www.DontBeServer.api.member.controller;

import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetReponseDto;
import com.dontbe.www.DontBeServer.api.member.service.MemberService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @DeleteMapping("withdrawal")
    public ResponseEntity<ApiResponse<Object>> withdrawalMember(Principal principal)
    {
        Long memberId = MemberUtil.getMemberId(principal);
        memberService.withdrawalMember(memberId);
        return ApiResponse.success(WITHDRAWAL_SUCCESS);
    }

    @GetMapping("member-data")
    public ResponseEntity<ApiResponse<MemberDetailGetReponseDto>> getMemberDetail(Principal principal){
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_DETAIL,memberService.getMemberDetail(memberId));
    }
}