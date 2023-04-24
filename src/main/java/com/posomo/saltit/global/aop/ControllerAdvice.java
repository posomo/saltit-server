package com.posomo.saltit.global.aop;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.posomo.saltit.domain.exception.NoRecordException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	protected String baseExceptionHandler(Exception e) {
		log.error("알 수 없는 에러가 발생했습니다.");
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return "서버 에러가 발생했습니다. 개발팀으로 문의 주세요.";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoRecordException.class)
	protected String noRecordExceptionHandler(NoRecordException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return "해당 레코드는 존재하지 않습니다.";
	}

	private String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
