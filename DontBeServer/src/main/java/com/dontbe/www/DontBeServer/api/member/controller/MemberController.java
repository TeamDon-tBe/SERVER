package com.dontbe.www.DontBeServer.api.member.controller;

import com.dontbe.www.DontBeServer.api.member.dto.request.MemberProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.request.MemberWithdrawalPatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.request.ProfilePatchRequestDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberDetailGetResponseDto;
import com.dontbe.www.DontBeServer.api.member.dto.response.MemberGetProfileResponseDto;
import com.dontbe.www.DontBeServer.api.member.service.MemberCommandService;
import com.dontbe.www.DontBeServer.api.member.service.MemberQueryService;
import com.dontbe.www.DontBeServer.common.response.ApiResponse;
import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import static com.dontbe.www.DontBeServer.common.response.SuccessStatus.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT Auth")
@Tag(name="멤버 관련",description = "Member Api Document")
public class MemberController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PatchMapping("withdrawal")
    @Operation(summary = "계정 삭제 API입니다.",description = "MemberWithdrawal")
    public ResponseEntity<ApiResponse<Object>> withdrawalMember(Principal principal, @RequestBody MemberWithdrawalPatchRequestDto memberWithdrawalPatchRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        memberCommandService.withdrawalMember(memberId, memberWithdrawalPatchRequestDto);
        return ApiResponse.success(WITHDRAWAL_SUCCESS);
    }

    @DeleteMapping("test-withdrawal")
    @Operation(summary = "앱 심사용 계정 삭제 API입니다.",description = "TestMemberWithdrawal")
    public ResponseEntity<ApiResponse<Object>> testWithdrawalMember(Principal principal)
    {
        Long memberId = MemberUtil.getMemberId(principal);
        memberCommandService.testWithdrawalMember(memberId);
        return ApiResponse.success(WITHDRAWAL_SUCCESS);
    }


    @GetMapping("member-data")
    @Operation(summary = "유저 상세 정보 조회 API입니다.",description = "MemberData")
    public ResponseEntity<ApiResponse<MemberDetailGetResponseDto>> getMemberDetail(Principal principal){
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_MEMBER_DETAIL, memberQueryService.getMemberDetail(memberId));
    }

    @GetMapping("viewmember/{viewmemberId}")
    @Operation(summary = "유저 프로필 정보 조회 API입니다.",description = "MemberProfile")
    public ResponseEntity<ApiResponse<MemberGetProfileResponseDto>> getMemberProfile(Principal principal,@PathVariable(name = "viewmemberId") Long viewmemberId) {
    return ApiResponse.success(GET_PROFILE_SUCCESS, memberQueryService.getMemberProfile(viewmemberId));
    }

    @PatchMapping("user-profile")
    @Operation(summary = "유저 프로필 수정 API입니다.",description = "UserProfilePatch")
    public ResponseEntity<ApiResponse<Object>> updateMemberProfile(Principal principal, @RequestBody MemberProfilePatchRequestDto memberProfilePatchRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        memberCommandService.updateMemberProfile(memberId, memberProfilePatchRequestDto);
        return ApiResponse.success(PATCH_MEMBER_PROFILE);
    }

    @PatchMapping(value = "user-profile2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 프로필 수정 API입니다.(이미지 수정 버전 추가)",description = "UserProfilePatch(+ProfileImage)")
    public ResponseEntity<ApiResponse<Object>> updateMemberProfile2(Principal principal, @RequestPart(value = "file", required = false) MultipartFile multipartFile, @RequestPart(value = "info", required = false) ProfilePatchRequestDto profilePatchRequestDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        memberCommandService.updateMemberProfile2(memberId, multipartFile, profilePatchRequestDto);
        return ApiResponse.success(PATCH_MEMBER_PROFILE);
    }

    @GetMapping("nickname-validation")
    @Operation(summary = "유저 닉네임 사용 가능 확인 API 입니다.",description = "NicknameValidation")
    public ResponseEntity<ApiResponse<Object>> checkMemberNickname(Principal principal, @RequestParam(value = "nickname") String nickname) {
        Long memberId = MemberUtil.getMemberId(principal);
        memberQueryService.checkNicknameValidate(memberId, nickname);
        return ApiResponse.success(NICKNAME_CHECK_SUCCESS);
    }
}