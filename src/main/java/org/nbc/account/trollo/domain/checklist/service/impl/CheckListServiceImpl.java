package org.nbc.account.trollo.domain.checklist.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.card.exception.NotFoundCardException;
import org.nbc.account.trollo.domain.card.repository.CardRepository;
import org.nbc.account.trollo.domain.checklist.dto.request.CheckListRequestDto;
import org.nbc.account.trollo.domain.checklist.entity.CheckList;
import org.nbc.account.trollo.domain.checklist.exception.NotFoundCheckListException;
import org.nbc.account.trollo.domain.checklist.repository.CheckListRepository;
import org.nbc.account.trollo.domain.checklist.service.CheckListService;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardRole;
import org.nbc.account.trollo.domain.userboard.exception.NotFoundUserBoardException;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckListServiceImpl implements CheckListService {

    private final CardRepository cardRepository;
    private final CheckListRepository checkListRepository;
    private final UserBoardRepository userBoardRepository;

    @Override
    public void createList(Long cardId, CheckListRequestDto requestDto,
        UserDetailsImpl userDetails) {
        String description = requestDto.description();
        boolean checkSign = requestDto.checkSign();
        User loginUser = userDetails.getUser();

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));

        Long boardId = card.getSection().getBoard().getId();

        checkUserInBoard(boardId, loginUser.getId());

        CheckList checkList = CheckList.builder()
            .card(card)
            .description(description)
            .checkSign(checkSign)
            .build();

        checkListRepository.save(checkList);
    }

    @Override
    @Transactional
    public void updateCheckList(Long cardId, Long checkListId, CheckListRequestDto requestDto,
        UserDetailsImpl userDetails) {
        String description = requestDto.description();
        boolean checkSign = requestDto.checkSign();
        User loginUser = userDetails.getUser();

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));
        CheckList checkList = checkListRepository.findById(checkListId)
            .orElseThrow(() -> new NotFoundCheckListException(ErrorCode.NOT_FOUND_CHECKLIST));

        Long boardId = card.getSection().getBoard().getId();
        checkUserInBoard(boardId, loginUser.getId());

        checkList.update(description, checkSign);
    }

    private void checkUserInBoard(Long boardId, Long userId) {
        UserBoard userBoard = userBoardRepository.findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new NotFoundUserBoardException(ErrorCode.NOT_FOUND_USER_BOARD));
        if (userBoard.getRole() == UserBoardRole.WAITING) {
            throw new ForbiddenAccessBoardException(ErrorCode.FORBIDDEN_ACCESS_BOARD);
        }
    }

}
