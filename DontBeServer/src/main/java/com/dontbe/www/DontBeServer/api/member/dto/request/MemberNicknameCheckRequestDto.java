package com.dontbe.www.DontBeServer.api.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberNicknameCheckRequestDto (
        @NotBlank String nickname
){
}
