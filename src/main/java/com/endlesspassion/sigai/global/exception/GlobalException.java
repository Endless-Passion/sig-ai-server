package com.endlesspassion.sigai.global.exception;

import com.endlesspassion.sigai.global.exception.code.GlobalErrorCode;

public class GlobalException extends SigaiException {
    public GlobalException(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}