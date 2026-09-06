package com.cy.auth.domain.user.application;

import com.cy.auth.domain.user.domain.refreshtoken.RefreshToken;
import com.cy.auth.domain.user.domain.refreshtoken.RefreshTokenRepository;
import com.cy.auth.domain.user.domain.user.Role;
import com.cy.auth.domain.user.domain.user.User;
import com.cy.auth.domain.user.domain.user.UserRepository;
import com.cy.auth.domain.user.presentation.dto.request.LoginRequest;
import com.cy.auth.domain.user.presentation.dto.request.LogoutRequest;
import com.cy.auth.domain.user.presentation.dto.request.ReissueRequest;
import com.cy.auth.domain.user.presentation.dto.request.SignUpRequest;
import com.cy.auth.domain.user.presentation.dto.response.TokenResponse;
import com.cy.auth.global.exception.BusinessException;
import com.cy.auth.global.exception.ErrorCode;;
import com.cy.auth.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    private static final String REFRESH_TOKEN = "refresh";

    public void signUp(SignUpRequest request){

        if (userRepository.existsByEmail(request.email()))
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        if (userRepository.existsByUsername(request.username()))
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->  new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId(), user.getRole());

        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId()).token(refreshToken).build());

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse reissue(ReissueRequest request){

        if (!jwtProvider.validateToken(request.refreshToken()))
            throw new BusinessException(ErrorCode.INVALID_TOKEN);

        if (!jwtProvider.getTokenType(request.refreshToken()).equals(REFRESH_TOKEN))
            throw new BusinessException(ErrorCode.INVALID_TOKEN);

        Long userId = jwtProvider.getAccountId(request.refreshToken());
        Role role = jwtProvider.getRole(request.refreshToken());

        RefreshToken savedRefreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));

        if (!savedRefreshToken.getToken().equals(request.refreshToken()))
            throw new BusinessException(ErrorCode.INVALID_TOKEN);

        String accessToken = jwtProvider.generateAccessToken(userId, role);
        String refreshToken = jwtProvider.generateRefreshToken(userId, role);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(userId)
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(newRefreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public void logout(LogoutRequest request){
        if (!jwtProvider.validateToken(request.refreshToken()))
            throw new BusinessException(ErrorCode.INVALID_TOKEN);

        if (!jwtProvider.getTokenType(request.refreshToken()).equals(REFRESH_TOKEN))
            throw new BusinessException(ErrorCode.INVALID_TOKEN);

        Long userId = jwtProvider.getAccountId(request.refreshToken());

        System.out.println("accountId = " + userId);
        System.out.println("before = " + refreshTokenRepository.existsById(userId));

        refreshTokenRepository.deleteById(userId);

        System.out.println("after = " + refreshTokenRepository.existsById(userId));
    }
}
