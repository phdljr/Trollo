package org.nbc.account.trollo.domain.invitation.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class InvitationDomainException extends CustomException {

    public InvitationDomainException(ErrorCode errorCode) {
        super(errorCode);
    }

}
