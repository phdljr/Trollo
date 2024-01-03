package org.nbc.account.trollo.domain.checklist.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundCheckListException extends CustomException {

    public NotFoundCheckListException(ErrorCode errorCode) {
        super(errorCode);
    }
}
