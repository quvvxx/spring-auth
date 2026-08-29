package com.cy.auth.domain.user.application;

import com.cy.auth.domain.user.domain.user.Role;
import com.cy.auth.domain.user.domain.user.User;
import com.cy.auth.domain.user.domain.user.UserRepository;
import com.cy.auth.domain.user.presentation.dto.request.LoginRequest;
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
    private final JwtProvider jwtProvider;

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

        return new TokenResponse(accessToken, refreshToken);
    }
}
