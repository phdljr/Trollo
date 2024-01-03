package org.nbc.account.trollo.domain.worker.repository;

import java.util.Optional;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.worker.entity.Worker;
import org.nbc.account.trollo.domain.worker.entity.WorkerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, WorkerId> {

    boolean existsByUserId(Long workerId);

    Optional<Worker> findByCardAndUser(Card card, User worker);
}
