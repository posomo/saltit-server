package com.posomo.saltit.domain.exception;

import com.posomo.saltit.global.constant.ErrorMessage;

public class SlackMessageException extends RuntimeException {
	public SlackMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public SlackMessageException(String message) {
		super(message);
	}

	public SlackMessageException() {
		super(ErrorMessage.SLACK_ERROR);
	}
}
