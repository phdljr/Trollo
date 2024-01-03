package org.nbc.account.trollo.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardListResponseDto(
    Long id,
    String name,
    String color

) {

}
