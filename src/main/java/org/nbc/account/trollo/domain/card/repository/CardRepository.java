package org.nbc.account.trollo.domain.card.repository;

import java.util.List;
import java.util.Optional;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllBySection_Board_Id(Long boardId);

    List<Card> findAllBySectionId(Long sectionId);

    Optional<Card> findBySectionIdAndNextCardIsNull(Long sectionId);

    boolean existsBySectionId(Long sectionId);
  
    Card findCardById(Long cardId);
}
