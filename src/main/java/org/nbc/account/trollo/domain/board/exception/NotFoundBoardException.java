package org.nbc.account.trollo.domain.board.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundBoardException extends CustomException {

    public NotFoundBoardException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
