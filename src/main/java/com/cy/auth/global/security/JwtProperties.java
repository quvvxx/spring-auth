package com.cy.auth.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {

    private final String secret;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;


}
