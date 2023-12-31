package org.nbc.account.trollo.domain.notification.event;

import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.notification.entity.NotificationEnum;


public record CardEvent(
    Card card,
    NotificationEnum notificationEnum) {

}
