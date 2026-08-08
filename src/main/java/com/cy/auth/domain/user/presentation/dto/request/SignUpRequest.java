package com.cy.auth.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,

        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        String password
){
}
