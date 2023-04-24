package com.posomo.saltit.domain.exception;

public class NoRecordException extends RuntimeException {
	public NoRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRecordException(String message) {
		super(message);
	}
}
