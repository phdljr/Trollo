package org.nbc.account.trollo.domain.section.repository;

import java.util.List;
import org.nbc.account.trollo.domain.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByBoardId(Long boardId);
}
