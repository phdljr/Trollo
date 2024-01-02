package org.nbc.account.trollo.domain.card.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.nbc.account.trollo.domain.checklist.dto.response.CheckListResponseDto;

@Builder
public record CardReadResponseDto(
    Long id,
    String title,
    String content,
    String color,
    LocalDateTime deadline,
    List<CheckListResponseDto> checkListResponseDtoList,
    double rate
) {

}
