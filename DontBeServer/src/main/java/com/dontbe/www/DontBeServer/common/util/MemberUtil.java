package com.dontbe.www.DontBeServer.common.util;

import lombok.RequiredArgsConstructor;
import com.dontbe.www.DontBeServer.common.exception.UnAuthorizedException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;

import java.security.Principal;

@RequiredArgsConstructor
public class MemberUtil {
    public static Long getMemberId(Principal principal) {
        if (principal == null) {
            throw new UnAuthorizedException(ErrorStatus.INVALID_MEMBER.getMessage());
        }
        return Long.valueOf(principal.getName());
    }
}
