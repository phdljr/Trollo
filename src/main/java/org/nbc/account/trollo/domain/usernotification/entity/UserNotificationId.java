package org.nbc.account.trollo.domain.usernotification.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotificationId implements Serializable {
    private Long user;
    private Long notification;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserNotificationId that = (UserNotificationId) o;
        return Objects.equals(user, that.user) && Objects.equals(notification, that.notification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, notification);
    }
}
