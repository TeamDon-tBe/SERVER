package com.dontbe.www.DontBeServer.common.util;

import com.dontbe.www.DontBeServer.common.exception.BadRequestException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class GhostUtil {
    public static int refineGhost(int input) {
        if (input > 0) {
            throw new BadRequestException(ErrorStatus.GHOST_HIGHLIMIT.getMessage());
        } else if (input < -85) {
            return -85;
        }
        return input;
    }

    public static void isGhostMember(int memberGhost) {
        if(memberGhost<=-85) {
            throw new BadRequestException(ErrorStatus.GHOST_USER.getMessage());
        }
    }
}
