package org.nbc.account.trollo.global.dto;

import lombok.Builder;
import org.springframework.validation.FieldError;

@Builder
public record BeanValidationErrorResponseDto(
    String field,
    String message
) {

    public BeanValidationErrorResponseDto(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
