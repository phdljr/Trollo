package org.nbc.account.trollo.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardRequestDto(
    String name,
    String color

) {

}
