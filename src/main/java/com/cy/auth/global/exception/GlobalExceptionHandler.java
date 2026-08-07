package com.cy.auth.global.exception;

import com.cy.auth.global.exception.response.ErrorData;
import com.cy.auth.global.exception.response.ErrorDetail;
import com.cy.auth.global.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse.of(ErrorData.from(errorCode));

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e){
        List<ErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorDetail.of(error.getField(), error.getDefaultMessage()))
                .toList();

        ErrorCode errorCode = ErrorCode.VALID_EXCEPTION;

        ErrorResponse response = ErrorResponse.of(ErrorData.of(errorCode, details));

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    public ResponseEntity<ErrorResponse> ExceptionHandler(Exception e){
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("예외가 발생했습니다.", e);

        ErrorResponse response = ErrorResponse.of(ErrorData.from(errorCode));

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

}
