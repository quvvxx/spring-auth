package com.cy.auth.domain.user.domain.refreshtoken;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
public class RefreshToken {
    @Id
    private Long userId;
    private String token;
}
