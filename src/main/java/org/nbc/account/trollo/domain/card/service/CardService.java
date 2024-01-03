package org.nbc.account.trollo.domain.card.service;

import java.util.List;
import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.card.dto.request.CardCreateRequestDto;
import org.nbc.account.trollo.domain.card.dto.request.CardUpdateRequestDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadDetailResponseDto;
import org.nbc.account.trollo.domain.user.entity.User;

public interface CardService {

    void createCard(final CardCreateRequestDto cardCreateRequestDto, Long boardId, Long sectionId,
        User user);

    CardReadDetailResponseDto getCard(Long cardId, User user);

    List<CardReadResponseDto> getCardAllByBoard(Long boardId, User user);

    List<CardReadResponseDto> getCardAllBySection(Long sectionId, User user);

    void updateCard(Long cardId, CardUpdateRequestDto cardUpdateRequestDto, User user);

    void deleteCard(Long cardId, User user);

    void changeCardSequence(Long fromCardId, Long toCardId, SequenceDirection direction,
        User user);

    void moveCardToSection(Long cardId, Long sectionId, User user);

    void addWorker(Long cardId, Long workerId, User user);

    void removeWorker(Long cardId, Long workerId, User user);
}
