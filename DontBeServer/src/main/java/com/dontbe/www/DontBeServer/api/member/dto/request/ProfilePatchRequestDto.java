package com.dontbe.www.DontBeServer.api.member.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ProfilePatchRequestDto (
        String nickname,
        Boolean isAlarmAllowed,
        String memberIntro
){
}
