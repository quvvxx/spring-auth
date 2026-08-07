package com.cy.auth.global.exception.response;

import com.cy.auth.global.exception.ErrorCode;
import java.util.List;

public record ErrorData(
        String code,
        String message,
        List<ErrorDetail> details
) {
    public static ErrorData from(ErrorCode errorCode){
        return new ErrorData(errorCode.name(), errorCode.getMessage(), null);
    }

    public static ErrorData of(ErrorCode errorCode, List<ErrorDetail> details){
        return new ErrorData(errorCode.getMessage(), errorCode.name(), details);
    }
}
