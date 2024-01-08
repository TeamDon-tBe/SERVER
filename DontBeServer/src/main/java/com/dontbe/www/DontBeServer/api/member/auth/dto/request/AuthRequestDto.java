package com.dontbe.www.DontBeServer.api.member.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuthRequestDto {
    private String socialPlatform;
}
