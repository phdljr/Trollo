package org.nbc.account.trollo.domain.board.dto;

import java.util.List;
import lombok.Builder;
import org.nbc.account.trollo.domain.section.dto.response.SectionReadResponseDto;

@Builder
public record BoardReadResponseDto(
    Long id,
    String name,
    List<SectionReadResponseDto> sections
) {

}
