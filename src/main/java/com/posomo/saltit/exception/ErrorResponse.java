package com.posomo.saltit.exception;

import lombok.Builder;

public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    @Builder
    public ErrorResponse(String code,String message,int status){
        this.code=code;
        this.message=message;
        this.status=status;
    }
}
