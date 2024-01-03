package org.nbc.account.trollo.domain.checklist.repository;

import java.util.List;
import org.nbc.account.trollo.domain.checklist.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    List<CheckList> findByCardId(Long cardId);

    List<CheckList> findByCardIdAndCheckSignIsTrue(Long cardId);
}
