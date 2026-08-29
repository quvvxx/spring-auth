package com.cy.auth.global.security;

import com.cy.auth.domain.user.domain.user.Role;
import com.cy.auth.global.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    public JwtProvider(JwtProperties properties, CustomUserDetailsService customUserDetailsService){
        this.jwtProperties = properties;
        this.customUserDetailsService = customUserDetailsService;
        this.secretKey = Keys.hmacShaKeyFor(this.jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long accountId, Role role) {
        return generateToken(accountId, ACCESS_TOKEN, jwtProperties.getAccessTokenExpiration(), role);
    }

    public String generateRefreshToken(Long accountId, Role role){
        return generateToken(accountId, REFRESH_TOKEN, jwtProperties.getRefreshTokenExpiration(), role);
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

    public Authentication getAuthentication(String token){
        Long userId = getAccountId(token);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(userId.toString());

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

}