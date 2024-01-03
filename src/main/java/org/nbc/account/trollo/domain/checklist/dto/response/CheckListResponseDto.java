package org.nbc.account.trollo.domain.checklist.dto.response;

import lombok.Builder;

@Builder
public record CheckListResponseDto(
    String description,
    boolean checkSign
) {

}

