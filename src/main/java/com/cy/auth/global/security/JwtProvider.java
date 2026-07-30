package com.cy.auth.global.security;

import com.cy.auth.domain.user.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties properties;
    private SecretKey secretKey;
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    @PostConstruct
    private void init(){
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long accountId, Role role) {
        return generateToken(accountId, ACCESS_TOKEN, properties.getAccessTokenExpiration(), role);
    }

    public String generateRefreshToken(Long accountId, Role role){
        return generateToken(accountId, REFRESH_TOKEN, properties.getRefreshTokenExpiration(), role);
    }

    private String generateToken(Long accountId, String type, Long time, Role role){

        Date now = new Date();

        return Jwts.builder()
                .subject(accountId.toString())
                .claim("type", type)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + time))
                .signWith(secretKey)
                .compact();

    }

    public boolean validateToken(String token){

        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        }

        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getAccountId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getRole(String token) {
        String role = getClaims(token).get("role", String.class);
        return Role.valueOf(role);
    }

    public String getTokenType(String token) {
        return getClaims(token).get("type", String.class);
    }

}
