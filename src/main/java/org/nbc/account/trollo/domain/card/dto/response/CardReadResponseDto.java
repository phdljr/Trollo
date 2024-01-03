package org.nbc.account.trollo.domain.card.dto.response;

import lombok.Builder;

@Builder
public record CardReadResponseDto(
    Long id,
    String title,
    String color
) {

}
