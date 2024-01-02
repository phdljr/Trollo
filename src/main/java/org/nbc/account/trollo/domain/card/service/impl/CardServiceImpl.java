package org.nbc.account.trollo.domain.card.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.exception.NotFoundBoardException;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.card.converter.CardSequenceDirection;
import org.nbc.account.trollo.domain.card.dto.request.CardCreateRequestDto;
import org.nbc.account.trollo.domain.card.dto.request.CardUpdateRequestDto;
import org.nbc.account.trollo.domain.card.dto.response.CardAllReadResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.card.entity.Card.CardBuilder;
import org.nbc.account.trollo.domain.card.exception.ForbiddenChangeCardSequenceException;
import org.nbc.account.trollo.domain.card.exception.IllegalChangeSameCardException;
import org.nbc.account.trollo.domain.card.exception.IllegalMoveToSectionException;
import org.nbc.account.trollo.domain.card.exception.NotFoundCardException;
import org.nbc.account.trollo.domain.card.mapper.CardMapper;
import org.nbc.account.trollo.domain.card.repository.CardRepository;
import org.nbc.account.trollo.domain.card.service.CardService;
import org.nbc.account.trollo.domain.notification.entity.NotificationType;
import org.nbc.account.trollo.domain.notification.event.CardEvent;
import org.nbc.account.trollo.domain.section.entity.Section;
import org.nbc.account.trollo.domain.section.exception.NotFoundSectionException;
import org.nbc.account.trollo.domain.section.exception.NotFoundSectionInBoardException;
import org.nbc.account.trollo.domain.section.repository.SectionRepository;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardRole;
import org.nbc.account.trollo.domain.userboard.exception.ForbiddenAccessBoardException;
import org.nbc.account.trollo.domain.userboard.exception.NotFoundUserBoardException;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;
    private final ApplicationEventPublisher publisher;
    private final SectionRepository sectionRepository;

    @Override
    @Transactional
    public void createCard(final CardCreateRequestDto cardCreateRequestDto, final Long boardId,
        final Long sectionId, final User user) {
        // 보드를 찾는다.
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        // 해당 보드에 사용자가 포함되어 있는지 확인한다.
        checkUserInBoard(boardId, user.getId());

        // 해당 보드에 색션이 있는지 찾는다.
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NotFoundSectionException(ErrorCode.NOT_FOUND_SECTION));
        if (!section.getBoard().getId().equals(board.getId())) {
            throw new NotFoundSectionInBoardException(ErrorCode.NOT_FOUND_SECTION_IN_BOARD);
        }

        CardBuilder cardBuilder = Card.builder()
            .title(cardCreateRequestDto.title())
            .prevCard(null)
            .nextCard(null)
            .section(section);

        // 해당 색션에 카드가 없다면, 그냥 추가한다.
        Optional<Card> lastCardOptional = cardRepository.findBySectionIdAndNextCardIsNull(
            section.getId());
        if (lastCardOptional.isEmpty()) {
            cardRepository.save(cardBuilder.build());
            return;
        }

        // 해당 색션에 카드가 있다면, 맨 마지막 카드의 상태를 변경하고 새로운 카드를 맨 마지막에 추가한다.
        Card lastCard = lastCardOptional.get();

        Card createdCard = cardBuilder.prevCard(lastCard).build();
        createdCard = cardRepository.save(createdCard);

        lastCard.setNextCard(createdCard);
        publisher.publishEvent(new CardEvent(board, user, NotificationType.CREATED));
    }

    @Override
    @Transactional(readOnly = true)
    public CardReadResponseDto getCard(final Long cardId, final User user) {
        // 해당 카드가 있는 보드에 사용자가 속하는지 확인한다.
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));

        Board board = card.getSection().getBoard();
        checkUserInBoard(board.getId(), user.getId());

        return CardMapper.INSTANCE.toCardReadResponseDto(card);
    }

    @Override
    public List<CardAllReadResponseDto> getCardAllByBoard(final Long boardId, final User user) {
        // 해당 보드에 사용자가 속하는지 확인한다.
        checkUserInBoard(boardId, user.getId());

        List<Card> cards = cardRepository.findAllBySection_Board_Id(boardId);
        return CardMapper.INSTANCE.toCardAllReadResponseDtoList(cards);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardAllReadResponseDto> getCardAllBySection(final Long sectionId, final User user) {
        // 색션이 속한 보드에 사용자가 속하는지 확인한다.
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NotFoundSectionException(ErrorCode.NOT_FOUND_SECTION));
        checkUserInBoard(section.getBoard().getId(), user.getId());

        List<Card> cards = cardRepository.findAllBySectionId(sectionId);
        return CardMapper.INSTANCE.toCardAllReadResponseDtoList(cards);
    }

    @Override
    @Transactional
    public void updateCard(final Long cardId, final CardUpdateRequestDto cardUpdateRequestDto,
        final User user) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));

        // 카드가 있는 보드에 사용자가 속하는지 확인한다.
        Long boardId = card.getSection().getBoard().getId();
        checkUserInBoard(boardId, user.getId());

        card.update(
            cardUpdateRequestDto.title(),
            cardUpdateRequestDto.content(),
            cardUpdateRequestDto.color(),
            cardUpdateRequestDto.deadline()
        );

        Board board = card.getSection().getBoard();
        publisher.publishEvent(new CardEvent(board, user, NotificationType.UPDATED));
    }

    @Override
    @Transactional
    public void deleteCard(final Long cardId, final User user) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));

        // 카드가 있는 보드에 사용자가 속하는지 확인한다.
        Long boardId = card.getSection().getBoard().getId();
        checkUserInBoard(boardId, user.getId());

        // 카드 삭제 시, 이전 카드와 다음 카드의 순서를 재설정한다.
        Card prevCard = card.getPrevCard();
        Card nextCard = card.getNextCard();
        card.getPrevCard().setNextCard(nextCard);
        card.getNextCard().setPrevCard(prevCard);

        cardRepository.delete(card);

        Board board = card.getSection().getBoard();
        publisher.publishEvent(new CardEvent(board, user, NotificationType.DELETED));
    }

    @Override
    @Transactional
    public void changeCardSequence(final Long fromCardId, final Long toCardId,
        final CardSequenceDirection direction,
        final User user) {
        Card fromCard = cardRepository.findById(fromCardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));
        Card toCard = cardRepository.findById(toCardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));

        Long fromCardboardId = fromCard.getSection().getBoard().getId();
        checkUserInBoard(fromCardboardId, user.getId());

        Long toCardboardId = toCard.getSection().getBoard().getId();
        checkUserInBoard(toCardboardId, user.getId());

        if (!Objects.equals(fromCardboardId, toCardboardId)) {
            throw new ForbiddenChangeCardSequenceException(ErrorCode.FORBIDDEN_CHANGE_CARD);
        }

        if (Objects.equals(fromCard, toCard)) {
            throw new IllegalChangeSameCardException(ErrorCode.ILLEGAL_CHANGE_SAME_CARD);
        }

        fromCard.changeSequence(toCard, direction);
    }

    @Override
    @Transactional
    public void moveCardToSection(final Long cardId, final Long sectionId, final User user) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));

        Long boardIdByCard = card.getSection().getBoard().getId();
        checkUserInBoard(boardIdByCard, user.getId());

        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NotFoundSectionException(ErrorCode.NOT_FOUND_SECTION));

        Long boardIdBySection = section.getBoard().getId();
        checkUserInBoard(boardIdBySection, user.getId());

        if(!Objects.equals(boardIdByCard, boardIdBySection)){
            throw new ForbiddenChangeCardSequenceException(ErrorCode.FORBIDDEN_CHANGE_CARD);
        }

        if(cardRepository.existsBySectionId(sectionId)){
            throw new IllegalMoveToSectionException(ErrorCode.ILLEGAL_MOVE_TO_SECTION);
        }

        card.changeSection(section);
    }

    private void checkUserInBoard(Long boardId, Long userId) {
        UserBoard userBoard = userBoardRepository.findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new NotFoundUserBoardException(ErrorCode.NOT_FOUND_USER_BOARD));
        if (userBoard.getRole() == UserBoardRole.WAITING) {
            throw new ForbiddenAccessBoardException(ErrorCode.FORBIDDEN_ACCESS_BOARD);
        }
    }

}
