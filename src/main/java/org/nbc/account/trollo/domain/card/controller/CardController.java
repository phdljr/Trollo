package org.nbc.account.trollo.domain.card.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.card.dto.request.CardCreateRequestDto;
import org.nbc.account.trollo.domain.card.dto.request.CardUpdateRequestDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadDetailResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.card.service.CardService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/boards/{boardId}/sections/{sectionId}/cards")
    public ApiResponse<Void> createCard(
        @PathVariable Long boardId,
        @PathVariable Long sectionId,
        @RequestBody CardCreateRequestDto cardCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.createCard(cardCreateRequestDto, boardId, sectionId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "카드 생성");
    }

    @GetMapping("/cards/{cardId}")
    public ApiResponse<CardReadDetailResponseDto> getCard(
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardReadDetailResponseDto responseDto = cardService.getCard(cardId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 조회", responseDto);
    }

    @GetMapping("/boards/{boardId}/cards")
    public ApiResponse<List<CardReadResponseDto>> getCardsByBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CardReadResponseDto> responseDto = cardService.getCardAllByBoard(boardId,
            userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 조회", responseDto);
    }

    @GetMapping("/sections/{sectionId}/cards")
    public ApiResponse<List<CardReadResponseDto>> getCardsBySection(
        @PathVariable Long sectionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CardReadResponseDto> responseDto = cardService.getCardAllBySection(sectionId,
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

    @DeleteMapping("/cards/{cardId}")
    public ApiResponse<Void> deleteCard(
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.deleteCard(cardId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 삭제");
    }

    @PutMapping("/cards/{fromCardId}/to/{toCardId}/{direction}")
    public ApiResponse<Void> changeCardSequence(
        @PathVariable Long fromCardId,
        @PathVariable Long toCardId,
        @PathVariable SequenceDirection direction,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.changeCardSequence(fromCardId, toCardId, direction, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 위치 변경");
    }

    @PutMapping("/cards/{cardId}/to/sections/{sectionId}")
    public ApiResponse<Void> moveCardToSection(
        @PathVariable Long cardId,
        @PathVariable Long sectionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.moveCardToSection(cardId, sectionId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "카드 위치 변경");
    }

    @PostMapping("/cards/{cardId}/workers/{workerId}")
    public ApiResponse<Void> addWorker(
        @PathVariable Long cardId,
        @PathVariable Long workerId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.addWorker(cardId, workerId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "작업자 추가");
    }

    @DeleteMapping("/cards/{cardId}/workers/{workerId}")
    public ApiResponse<Void> removeWorker(
        @PathVariable Long cardId,
        @PathVariable Long workerId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.removeWorker(cardId, workerId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "작업자 삭제");
    }

}
