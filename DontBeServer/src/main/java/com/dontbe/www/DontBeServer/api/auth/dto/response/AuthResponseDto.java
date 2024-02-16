package com.dontbe.www.DontBeServer.api.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AuthResponseDto {

    private String nickName;

    private Long memberId;

    private String accessToken;

    private String refreshToken;

    private String memberProfileUrl;

    private Boolean isNewUser;

    private Boolean isDeleted;
}

