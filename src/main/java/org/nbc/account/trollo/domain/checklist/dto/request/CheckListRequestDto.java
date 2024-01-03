package org.nbc.account.trollo.domain.checklist.dto.request;

import lombok.Builder;

@Builder
public record CheckListRequestDto(
    String description
) {

}
