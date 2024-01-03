package org.nbc.account.trollo.domain.section.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundSectionException extends CustomException {

    public NotFoundSectionException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
