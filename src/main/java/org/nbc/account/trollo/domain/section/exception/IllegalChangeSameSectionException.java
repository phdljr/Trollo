package org.nbc.account.trollo.domain.section.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class IllegalChangeSameSectionException extends CustomException {

    public IllegalChangeSameSectionException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
