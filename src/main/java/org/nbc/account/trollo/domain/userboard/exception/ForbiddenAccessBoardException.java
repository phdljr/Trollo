package org.nbc.account.trollo.domain.userboard.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class ForbiddenAccessBoardException extends CustomException {

    public ForbiddenAccessBoardException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
