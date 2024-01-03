package org.nbc.account.trollo.domain.usernotification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.notification.entity.Notification;
import org.nbc.account.trollo.domain.notification.event.CardEvent;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardId;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_USER_NOTIFICATION")
@IdClass(UserNotificationId.class)
public class UserNotification {

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Enumerated(EnumType.STRING)
    private UserNotificationStatus status;

    public UserNotification(Notification notification,User user){
        this.user = user;
        this.notification = notification;
        this.status = UserNotificationStatus.UNREAD;
    }

    public void change() {
        this.status = UserNotificationStatus.READ;
    }
}
