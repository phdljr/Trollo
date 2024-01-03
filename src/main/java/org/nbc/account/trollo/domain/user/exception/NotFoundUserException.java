package org.nbc.account.trollo.domain.user.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundUserException extends CustomException {

    public NotFoundUserException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
