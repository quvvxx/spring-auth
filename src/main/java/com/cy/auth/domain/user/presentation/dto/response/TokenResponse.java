package com.cy.auth.domain.user.presentation.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
