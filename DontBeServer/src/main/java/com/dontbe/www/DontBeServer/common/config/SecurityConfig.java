package com.dontbe.www.DontBeServer.common.config;

import com.dontbe.www.DontBeServer.common.config.jwt.JwtAuthenticationEntryPoint;
import com.dontbe.www.DontBeServer.common.config.jwt.JwtAuthenticationFilter;
import com.dontbe.www.DontBeServer.common.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private static final String[] SWAGGER_URL = {
//            "/swagger-resources/**",
//            "/favicon.ico",
//            "/api-docs/**",
//            "/swagger-ui/**",
//            "/swagger-ui.html",
//            "/swagger-ui/index.html",
//            "/docs/swagger-ui/index.html",
//            "/swagger-ui/swagger-ui.css",
//    };
//
//    @Bean
////    @Profile("dev")
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        // XorCsrfTokenRequestAttributeHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();
////        http
////                .csrf((csrf) -> csrf
////                        .csrfTokenRequestHandler(requestHandler)
////                )
////                .authorizeRequests()
////                .anyRequest().permitAll();
//        http
//                .csrf().disable()
//                .httpBasic().disable()
//                .authorizeHttpRequests()
//                .anyRequest().permitAll();
//
//        return http.build();
//    }
//}
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth",
            "/health",
            "/profile",
            "/actuator/**"
    };

    @Bean
//    @Profile("dev")
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf((csrfConfig) -> csrfConfig.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(SWAGGER_URL).permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(
                        new JwtAuthenticationFilter(this.jwtTokenProvider, this.jwtAuthenticationEntryPoint),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(this.jwtAuthenticationEntryPoint));
        return http.build();
    }
}
