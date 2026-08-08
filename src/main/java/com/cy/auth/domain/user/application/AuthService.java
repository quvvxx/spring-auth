package com.cy.auth.domain.user.application;

import com.cy.auth.domain.user.domain.Role;
import com.cy.auth.domain.user.domain.User;
import com.cy.auth.domain.user.domain.UserRepository;
import com.cy.auth.domain.user.presentation.dto.request.SignUpRequest;
import com.cy.auth.global.exception.BusinessException;
import com.cy.auth.global.exception.ErrorCode;;
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
}
