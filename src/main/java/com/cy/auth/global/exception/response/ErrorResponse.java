package com.cy.auth.global.exception.response;

public record ErrorResponse(
        boolean success,
        ErrorData errorData

) {
    public static ErrorResponse of(ErrorData errorData){
        return new ErrorResponse(false, errorData);
    }
}
