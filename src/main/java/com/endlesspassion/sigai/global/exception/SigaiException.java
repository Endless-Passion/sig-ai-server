package com.endlesspassion.sigai.global.exception;

import com.endlesspassion.sigai.global.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class SigaiException extends RuntimeException {
    private final ErrorCode errorCode;

    public SigaiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}