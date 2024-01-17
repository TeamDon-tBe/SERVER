package com.dontbe.www.DontBeServer.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Don't be API 명세서",
                description = "Don't be API 명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
@SecurityScheme(
        name = "JWT Auth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi Version1OpenApi() {
        return GroupedOpenApi.builder()
                .group("Don't be API v1")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
