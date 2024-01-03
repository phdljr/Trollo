package org.nbc.account.trollo.domain.section.dto.response;

import java.util.List;
import lombok.Builder;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;

@Builder
public record SectionReadResponseDto(
    Long id,
    String name,
    List<CardReadResponseDto> cards
) {

}
