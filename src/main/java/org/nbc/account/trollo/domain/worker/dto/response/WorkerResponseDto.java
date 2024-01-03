package org.nbc.account.trollo.domain.worker.dto.response;

import lombok.Builder;

@Builder
public record WorkerResponseDto(
    Long id,
    String nickname
) {

}
