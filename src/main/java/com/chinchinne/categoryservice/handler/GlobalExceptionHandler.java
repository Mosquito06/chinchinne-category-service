package com.chinchinne.categoryservice.handler;

import com.chinchinne.categoryservice.exception.CustomException;
import com.chinchinne.categoryservice.model.ErrorCode;
import com.chinchinne.categoryservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex)
    {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        switch (ex.getErrorCode())
        {
            case NOT_FOUND_RECORD:
                status = HttpStatus.BAD_REQUEST.value();
                break;
        }

        ErrorResponse res = ErrorResponse.builder()
                                        .status(ex.getErrorCode().getCode())
                                        .error(ex.getErrorCode().name())
                                        .code(ex.getErrorCode().getCode())
                                        .message(ex.getErrorCode().getDetail())
                                        .build();

        return ResponseEntity.status(status).body(res);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException()
    {
        ErrorResponse res = ErrorResponse.builder()
                                        .status(ErrorCode.INVALID_REQUEST.getCode())
                                        .error(ErrorCode.INVALID_REQUEST.name())
                                        .code(ErrorCode.INVALID_REQUEST.getCode())
                                        .message(ErrorCode.INVALID_REQUEST.getDetail())
                                        .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
