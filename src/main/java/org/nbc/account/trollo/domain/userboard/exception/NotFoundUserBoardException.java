package org.nbc.account.trollo.domain.userboard.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundUserBoardException extends CustomException {

    public NotFoundUserBoardException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
