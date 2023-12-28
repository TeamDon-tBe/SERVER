package com.dontbe.www.DontBeServer.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_URL = {
            "/swagger-resources/**",
            "/favicon.ico",
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/docs/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
    };

    @Bean
//    @Profile("dev")
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // XorCsrfTokenRequestAttributeHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();
//        http
//                .csrf((csrf) -> csrf
//                        .csrfTokenRequestHandler(requestHandler)
//                )
//                .authorizeRequests()
//                .anyRequest().permitAll();
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeHttpRequests()
                .anyRequest().permitAll();

        return http.build();
    }
}

