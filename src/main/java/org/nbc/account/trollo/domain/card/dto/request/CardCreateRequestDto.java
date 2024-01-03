package org.nbc.account.trollo.domain.card.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CardCreateRequestDto(
    @Size(max = 500)
    String title
) {

}
