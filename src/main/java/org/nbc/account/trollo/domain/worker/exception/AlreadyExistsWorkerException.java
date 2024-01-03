package org.nbc.account.trollo.domain.worker.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class AlreadyExistsWorkerException extends CustomException {

    public AlreadyExistsWorkerException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
