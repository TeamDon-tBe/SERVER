package com.dontbe.www.DontBeServer.api.member.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialInfoDto {
    private String id;
    private String nickname;
    private String profileUrl;
}