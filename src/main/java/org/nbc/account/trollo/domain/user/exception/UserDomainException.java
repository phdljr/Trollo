package org.nbc.account.trollo.domain.user.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class UserDomainException extends CustomException {

    public UserDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
