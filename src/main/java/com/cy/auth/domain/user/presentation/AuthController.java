package com.cy.auth.domain.user.presentation;

import com.cy.auth.domain.user.application.AuthService;
import com.cy.auth.domain.user.presentation.dto.request.SignUpRequest;
import com.cy.auth.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpRequest request){
        authService.signUp(request);
        return ApiResponse.ok("회원가입이 성공적으로 완료되었습니다.");
    }
}
