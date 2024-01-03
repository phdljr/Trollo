package org.nbc.account.trollo.domain.card.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class BadCardSequenceDirectionRequestException extends CustomException {

    public BadCardSequenceDirectionRequestException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
