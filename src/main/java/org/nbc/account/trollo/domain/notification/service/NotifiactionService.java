package org.nbc.account.trollo.domain.notification.service;

import java.util.List;
import org.nbc.account.trollo.domain.notification.dto.response.NotificationResponseDto;
import org.nbc.account.trollo.global.security.UserDetailsImpl;

public interface NotifiactionService {

    List<NotificationResponseDto> getNotifications(Long boardId, UserDetailsImpl userDetails);

}
