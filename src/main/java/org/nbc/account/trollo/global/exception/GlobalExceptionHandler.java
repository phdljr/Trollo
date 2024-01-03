package org.nbc.account.trollo.global.exception;

import java.util.List;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.dto.BeanValidationErrorResponseDto;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> handleCustomException(CustomException customException) {
        ErrorCode errorCode = customException.getErrorCode();
        return new ApiResponse<>(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<List<BeanValidationErrorResponseDto>> handleMethodArgumentNotValidException(
        BindingResult bindingResult) {
        ErrorCode errorCode = ErrorCode.BAD_FORM;
        List<BeanValidationErrorResponseDto> responseDto = bindingResult.getFieldErrors().stream()
            .map(BeanValidationErrorResponseDto::new)
            .toList();
        return new ApiResponse<>(errorCode.getHttpStatus().value(), "잘못된 입력값", responseDto);
    }

}
