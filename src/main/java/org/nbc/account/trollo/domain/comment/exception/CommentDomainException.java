package org.nbc.account.trollo.domain.comment.exception;

import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;

public class CommentDomainException extends CustomException {

    public CommentDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
