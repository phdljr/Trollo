package org.nbc.account.trollo.domain.section.service;

import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.section.dto.SectionCreateRequestDto;
import org.nbc.account.trollo.domain.user.entity.User;

public interface SectionService {

    void createSection(Long boardId, SectionCreateRequestDto sectionCreateRequestDto, User user);

    void updateSection(Long sectionId, SectionCreateRequestDto sectionCreateRequestDto, User user);

    void deleteSection(Long sectionId, User user);

    void changeSectionSequence(Long fromSectionId, Long toSectionId, SequenceDirection direction,
        User user);
}
