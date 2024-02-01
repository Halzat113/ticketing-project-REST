package com.example.ticketingprojectrest.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public  class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;
    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;


    @Bean
    public SecurityFilterChain configure (HttpSecurity http)  throws Exception {

    //    http.authorizeHttpRequests(authz->authz.anyRequest().authenticated());
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz->authz.requestMatchers("/swagger-ui/index.html").permitAll().anyRequest().authenticated())
                .httpBasic(httpBasic->httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2->oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter))
                        .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler));
        http
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }




}
