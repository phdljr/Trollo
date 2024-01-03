package org.nbc.account.trollo.domain.board.combine.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.combine.BoardCombine;
import org.nbc.account.trollo.domain.board.dto.BoardReadResponseDto;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.exception.NotFoundBoardException;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.card.service.CardService;
import org.nbc.account.trollo.domain.section.dto.response.SectionReadResponseDto;
import org.nbc.account.trollo.domain.section.entity.Section;
import org.nbc.account.trollo.domain.section.exception.NotFoundFirstSectionException;
import org.nbc.account.trollo.domain.section.repository.SectionRepository;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardCombineImpl implements BoardCombine {

    private final BoardRepository boardRepository;
    private final SectionRepository sectionRepository;
    private final CardService cardService;

    @Override
    @Transactional
    public BoardReadResponseDto getCardAndSectionInBoard(final Long boardId, final User user) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundBoardException(
                ErrorCode.NOT_FOUND_BOARD));

        List<Section> sections = sectionRepository.findByBoardId(boardId);
        List<Section> sortedSections = sortSections(sections);

        List<SectionReadResponseDto> sectionDto = sortedSections.stream()
            .map(section -> SectionReadResponseDto.builder()
                .id(section.getId())
                .name(section.getName())
                .cards(cardService.getCardAllBySection(section.getId(), user))
                .build())
            .toList();

        return BoardReadResponseDto.builder()
            .id(boardId)
            .name(board.getName())
            .sections(sectionDto)
            .build();
    }

    private List<Section> sortSections(final List<Section> sections) {
        List<Section> result = new ArrayList<>();

        Section section = sections.stream()
            .filter(s -> s.getPrevSection() == null)
            .findFirst()
            .orElse(null);

        while (section != null) {
            result.add(section);
            section = section.getNextSection();
        }

        return result;
    }
}
