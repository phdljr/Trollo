package org.nbc.account.trollo.domain.userboard.repository;

import java.util.Optional;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBoardRepository extends JpaRepository<UserBoard, UserBoardId> {

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    Optional<UserBoard> findByBoardIdAndUserId(Long boardId, Long userId);
}
