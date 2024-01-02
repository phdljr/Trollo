package org.nbc.account.trollo.domain.card.service;

import java.util.List;
import org.nbc.account.trollo.domain.card.converter.CardSequenceDirection;
import org.nbc.account.trollo.domain.card.dto.request.CardCreateRequestDto;
import org.nbc.account.trollo.domain.card.dto.request.CardUpdateRequestDto;
import org.nbc.account.trollo.domain.card.dto.response.CardAllReadResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.user.entity.User;

public interface CardService {

    void createCard(final CardCreateRequestDto cardCreateRequestDto, Long boardId, Long sectionId,
        User user);

    CardReadResponseDto getCard(Long cardId, User user);

    List<CardAllReadResponseDto> getCardAllByBoard(Long boardId, User user);

    List<CardAllReadResponseDto> getCardAllBySection(Long sectionId, User user);

    void updateCard(Long cardId, CardUpdateRequestDto cardUpdateRequestDto, User user);

    void deleteCard(Long cardId, User user);

    void changeCardSequence(Long fromCardId, Long toCardId, CardSequenceDirection direction,
        User user);

    void moveCardToSection(Long cardId, Long sectionId, User user);
}
