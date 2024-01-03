package org.nbc.account.trollo.domain.notification.event;

import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.notification.entity.NotificationType;
import org.nbc.account.trollo.domain.user.entity.User;


public record CardEvent(
    Board board,
    User user,
    NotificationType notificationType) {

}
