package com.dontbe.www.DontBeServer.api;

import com.dontbe.www.DontBeServer.common.util.MemberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Tag(name = "HealthCheck Controller", description = "HealthCheck API Document")
public class HealthCheckController {

    @GetMapping("health")
    @Operation(summary = "HealthCheck", description = "HealthCheck API입니다.")
    public Long healthCheck(Principal principal) {
        return MemberUtil.getMemberId(principal);
//        return "OK";
    }
}
