package com.posomo.saltit.domain.exception;

import com.posomo.saltit.global.constant.ErrorMessage;

public class NoRecordException extends RuntimeException {
	public NoRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRecordException(String message) {
		super(message);
	}

	public NoRecordException() {
		super(ErrorMessage.RECORD_NOT_FOUND);
	}
}
