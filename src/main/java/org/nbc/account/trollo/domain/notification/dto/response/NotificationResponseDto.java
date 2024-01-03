package org.nbc.account.trollo.domain.notification.dto.response;

import java.time.LocalDateTime;
import org.nbc.account.trollo.domain.notification.entity.NotificationType;

public record NotificationResponseDto(
    NotificationType fieldName,
    String fieldContent,
    LocalDateTime createdAt
) {

}
