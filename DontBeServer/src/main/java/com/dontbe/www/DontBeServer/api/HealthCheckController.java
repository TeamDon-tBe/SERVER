package com.dontbe.www.DontBeServer.api;

import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HealthCheckController {

    @GetMapping("health")
    public Long healthCheck(Principal principal) {
        return MemberUtil.getMemberId(principal);
//        return "OK";
    }
}
