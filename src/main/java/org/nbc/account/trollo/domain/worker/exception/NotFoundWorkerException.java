package org.nbc.account.trollo.domain.worker.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class NotFoundWorkerException extends CustomException {

    public NotFoundWorkerException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
