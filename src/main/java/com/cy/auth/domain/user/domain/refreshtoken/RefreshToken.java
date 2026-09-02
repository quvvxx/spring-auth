package com.cy.auth.domain.user.domain.refreshtoken;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@RedisHash(value = "refreshToken", timeToLive = 1209600)
public class RefreshToken {
    @Id
    private Long userId;
    private String token;
}
