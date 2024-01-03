package org.nbc.account.trollo.domain.notification.event;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.notification.entity.Notification;
import org.nbc.account.trollo.domain.notification.repository.NotificationRepository;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.domain.usernotification.entity.UserNotification;
import org.nbc.account.trollo.domain.usernotification.repository.UserNotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationListeneer {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserBoardRepository userBoardRepository;

    @EventListener
    public void recordNotification(CardEvent event) {
        Notification notification = new Notification(event);
        notificationRepository.save(notification);

        List<UserBoard> userBoards = userBoardRepository.findAllByBoardId(
            notification.getBoard().getId());

        for (UserBoard userBoard : userBoards) {
            User user = userBoard.getUser();
            UserNotification userNotification = new UserNotification(notification, user);
            userNotificationRepository.save(userNotification);
        }
    }

}
