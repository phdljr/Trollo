package org.nbc.account.trollo.domain.notification.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundUnreadStatusException extends CustomException {

    public NotFoundUnreadStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
