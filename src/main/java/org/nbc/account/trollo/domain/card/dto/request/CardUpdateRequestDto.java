package org.nbc.account.trollo.domain.card.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CardUpdateRequestDto(
    String title,
    String content,
    String color,
    LocalDateTime deadline
) {

}
