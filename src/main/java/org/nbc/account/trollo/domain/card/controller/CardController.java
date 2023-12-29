package org.nbc.account.trollo.domain.card.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.card.dto.request.CardCreateRequestDto;
import org.nbc.account.trollo.domain.card.dto.request.CardUpdateRequestDto;
import org.nbc.account.trollo.domain.card.dto.response.CardAllReadResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.card.service.CardService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CardController {

    private final CardService cardService;

    @PostMapping("/boards/{boardId}/columns/{columnId}/cards")
    public ApiResponse<Void> createCard(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody CardCreateRequestDto cardCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.createCard(cardCreateRequestDto, boardId, columnId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "카드 생성");
    }

    @GetMapping("/cards/{cardId}")
    public ApiResponse<CardReadResponseDto> getCard(
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardReadResponseDto responseDto = cardService.getCard(cardId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 조회", responseDto);
    }

    @GetMapping("/boards/{boardId}/cards")
    public ApiResponse<List<CardAllReadResponseDto>> getCardsByBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CardAllReadResponseDto> responseDto = cardService.getCardAllByBoard(boardId,
            userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 조회", responseDto);
    }

    @GetMapping("/sections/{sectionId}/cards")
    public ApiResponse<List<CardAllReadResponseDto>> getCardsBySection(
        @PathVariable Long sectionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CardAllReadResponseDto> responseDto = cardService.getCardAllBySection(sectionId,
            userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 조회", responseDto);
    }

    @PutMapping("/cards/{cardId}")
    public ApiResponse<Void> updateCard(
        @PathVariable Long cardId,
        @RequestBody CardUpdateRequestDto cardUpdateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.updateCard(cardId, cardUpdateRequestDto, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 수정");
    }

}
