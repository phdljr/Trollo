package org.nbc.account.trollo.domain.notification.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.exception.NotFoundBoardException;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.card.exception.ForbiddenAccessBoardException;
import org.nbc.account.trollo.domain.notification.dto.response.NotificationResponseDto;
import org.nbc.account.trollo.domain.notification.entity.Notification;
import org.nbc.account.trollo.domain.notification.entity.NotificationType;
import org.nbc.account.trollo.domain.notification.repository.NotificationRepository;
import org.nbc.account.trollo.domain.notification.service.NotifiactionService;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardRole;
import org.nbc.account.trollo.domain.userboard.exception.NotFoundUserBoardException;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.domain.usernotification.entity.UserNotification;
import org.nbc.account.trollo.domain.usernotification.entity.UserNotificationStatus;
import org.nbc.account.trollo.domain.usernotification.repository.UserNotificationRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotifiactionService {

    private final NotificationRepository notificationRepository;
    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;
    private final UserNotificationRepository userNotificationRepository;

    @Override
    @Transactional
    public List<NotificationResponseDto> getNotifications(Long boardId,
        UserDetailsImpl userDetails) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        checkUserInBoard(boardId, userDetails.getUser().getId());

        List<UserNotification> unreadStatus = userNotificationRepository.findAllByUserIdAndStatus(
            userDetails.getUser().getId()
            , UserNotificationStatus.UNREAD);

        List<Notification> notificationList = notificationRepository.findByBoardId(boardId);
        List<NotificationResponseDto> notificationResponseDtos = new ArrayList<>();

        for (Notification notification : notificationList) {
            NotificationType notificationEnum = notification.getFieldName();
            String message = notification.getFieldContent();
            LocalDateTime createdAt = notification.getCreatedAt();
            notificationResponseDtos.add(
                new NotificationResponseDto(notificationEnum, message, createdAt));
        }
        changeNotificationStatus(unreadStatus);

        return notificationResponseDtos;
    }

    private void checkUserInBoard(Long boardId, Long userId) {
        UserBoard userBoard = userBoardRepository.findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new NotFoundUserBoardException(ErrorCode.NOT_FOUND_USER_BOARD));
        if (userBoard.getRole() == UserBoardRole.WAITING) {
            throw new ForbiddenAccessBoardException(ErrorCode.FORBIDDEN_ACCESS_BOARD);
        }
    }


    private void changeNotificationStatus(List<UserNotification> unreadStatus) {
        for (UserNotification status : unreadStatus) {
            status.change();
        }
    }
}
