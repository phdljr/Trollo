package org.nbc.account.trollo.domain.notification.repository;

import java.util.List;
import org.nbc.account.trollo.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByBoardId(Long boardId);
}
