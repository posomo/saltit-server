package com.posomo.saltit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied");
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
    public static ErrorResponse enumToEntity(ErrorCode errorCode){
        return ErrorResponse.builder().code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .build();
    }
}
