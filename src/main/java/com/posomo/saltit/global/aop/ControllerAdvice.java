package com.posomo.saltit.global.aop;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.global.constant.ErrorMessage;
import com.posomo.saltit.global.constant.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	protected String baseExceptionHandler(Exception e) {
		log.error(ErrorMessage.UNKNOWN_ERROR);
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.UNKNOWN_ERROR;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoRecordException.class)
	protected String noRecordExceptionHandler(NoRecordException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.RECODE_NOT_FOUND;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected String methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.MISMATCH_PARAM;
	}

	private String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
