package org.nbc.account.trollo.domain.section.service.impl;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.card.exception.NotFoundCardException;
import org.nbc.account.trollo.domain.section.dto.SectionCreateRequestDto;
import org.nbc.account.trollo.domain.section.entity.Section;
import org.nbc.account.trollo.domain.section.exception.IllegalChangeSameSectionException;
import org.nbc.account.trollo.domain.section.exception.NotFoundSectionException;
import org.nbc.account.trollo.domain.section.repository.SectionRepository;
import org.nbc.account.trollo.domain.section.service.SectionService;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.exception.NotFoundUserBoardException;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final UserBoardRepository userBoardRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public void createSection(Long boardId, SectionCreateRequestDto sectionCreateRequestDto,
        User user) {
        checkUserInBoard(boardId, user.getId());
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundSectionException(ErrorCode.NOT_FOUND_SECTION));

        Section section = Section.builder()
            .board(board)
            .name(sectionCreateRequestDto.name())
            .build();

        Section lastSection = sectionRepository.findByBoardId(boardId).stream()
            .filter(s -> s.getNextSection() == null)
            .findFirst()
            .orElse(null);

        if(lastSection != null)
            lastSection.setNextSection(section);

        section.setPrevSection(lastSection);

        sectionRepository.save(section);
    }

    @Override
    @Transactional
    public void updateSection(Long sectionId, SectionCreateRequestDto sectionCreateRequestDto,
        User user) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));
        Long boardId = section.getBoard().getId();
        checkUserInBoard(boardId, user.getId());

        section.update(
            sectionCreateRequestDto.name()
        );

    }

    @Override
    @Transactional
    public void deleteSection(Long sectionId, User user) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NotFoundCardException(ErrorCode.NOT_FOUND_CARD));
        Long boardId = section.getBoard().getId();
        checkUserInBoard(boardId, user.getId());

//        Section prevSection = section.getPrevSection();
//        Section nextSection = section.getNextSection();
//        section.getPrevSection().setNextSection(nextSection);
//        section.getNextSection().setPrevSection(prevSection);

        sectionRepository.delete(section);
    }

    @Override
    @Transactional
    public void changeSectionSequence(final Long fromSectionId, final Long toSectionId,
        final SequenceDirection direction, final User user) {
        Section fromSection = sectionRepository.findById(fromSectionId)
            .orElseThrow(() -> new NotFoundSectionException(ErrorCode.NOT_FOUND_SECTION));
        Long boardId = fromSection.getBoard().getId();
        checkUserInBoard(boardId, user.getId());

        Section toSection = sectionRepository.findById(toSectionId)
            .orElseThrow(() -> new NotFoundSectionException(ErrorCode.NOT_FOUND_SECTION));
        boardId = toSection.getBoard().getId();
        checkUserInBoard(boardId, user.getId());

        if (Objects.equals(fromSection, toSection)) {
            throw new IllegalChangeSameSectionException(ErrorCode.ILLEGAL_CHANGE_SAME_SECTION);
        }

        fromSection.changeSequence(toSection, direction);
    }

    private void checkUserInBoard(Long boardId, Long userId) {
        userBoardRepository.findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new NotFoundUserBoardException(ErrorCode.NOT_FOUND_USER_BOARD));
    }
}

