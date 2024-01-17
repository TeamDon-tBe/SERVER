package com.dontbe.www.DontBeServer.api.member.controller;

import com.dontbe.www.DontBeServer.api.member.dto.request.MemberClickGhostRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberNicknameCheckRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetResponseDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberGetProfileResponseDto;
import com.dontbe.www.DontBeServer.api.member.service.MemberService;
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

import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Auth")
@Tag(name="멤버 관련",description = "Member Api Document")
public class MemberController {
    private final MemberService memberService;

    @DeleteMapping("withdrawal")
    @Operation(summary = "유저 탈퇴 API입니다. 앱잼 기간 미사용",description = "MemberWithdrawal")
    public ResponseEntity<ApiResponse<Object>> withdrawalMember(Principal principal)
    {
        Long memberId = MemberUtil.getMemberId(principal);
        memberService.withdrawalMember(memberId);
        return ApiResponse.success(WITHDRAWAL_SUCCESS);
    }

    @GetMapping("member-data")
    @Operation(summary = "유저 상세 정보 조회 API입니다.",description = "MemberData")
    public ResponseEntity<ApiResponse<MemberDetailGetResponseDto>> getMemberDetail(Principal principal){
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_DETAIL, memberService.getMemberDetail(memberId));
    }

    @GetMapping("viewmember/{viewmemberId}")
    @Operation(summary = "유저 프로필 정보 조회 API입니다.",description = "MemberProfile")
    public ResponseEntity<ApiResponse<MemberGetProfileResponseDto>> getMemberProfile(Principal principal,@PathVariable(name = "viewmemberId") Long viewmemberId) {
        return ApiResponse.success(GET_PROFILE_SUCCESS, memberService.getMemberProfile(viewmemberId));
    }

    @PostMapping("ghost")
    @Operation(summary = "투명도 낮추는 API입니다.",description = "MemberGhost")
    public ResponseEntity<ApiResponse<Object>> clickMemberGhost(Principal principal,@RequestBody MemberClickGhostRequestDto memberClickGhostRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        memberService.clickMemberGhost(memberId, memberClickGhostRequestDto);
        return ApiResponse.success(CLICK_MEMBER_GHOST_SUCCESS);
    }

    @PatchMapping("user-profile")
    @Operation(summary = "유저 프로필 수정 API입니다.",description = "UserProfilePatch")
    public ResponseEntity<ApiResponse<Object>> updateMemberProfile(Principal principal, @RequestBody MemberProfilePatchRequestDto memberProfilePatchRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        memberService.updateMemberProfile(memberId, memberProfilePatchRequestDto);
        return ApiResponse.success(PATCH_MEMBER_PROFILE);
    }

    @GetMapping("nickname-validation")
    @Operation(summary = "유저 닉네임 사용 가능 확인 API 입니다.",description = "NicknameValidation")
    public ResponseEntity<ApiResponse<Object>> checkMemberNickname(Principal principal,@RequestParam(value = "nickname") String nickname) {
        memberService.checkNicknameValidate(nickname);
        return ApiResponse.success(NICKNAME_CHECK_SUCCESS);
    }
}