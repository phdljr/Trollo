package org.nbc.account.trollo.domain.card.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundCardException extends CustomException {

    public NotFoundCardException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
