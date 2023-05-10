package com.posomo.saltit.global.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.global.constant.ErrorMessage;
import com.posomo.saltit.global.constant.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerAdviceV1 implements ControllerAdvice {
	@Override
	public String baseExceptionHandler(Exception e) {
		log.error(ErrorMessage.UNKNOWN_ERROR);
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.UNKNOWN_ERROR;
	}

	@Override
	public String noRecordExceptionHandler(NoRecordException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.RECODE_NOT_FOUND;
	}

	@Override
	public String methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.MISMATCH_PARAM;
	}

	@Override
	public String methodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));

		StringBuilder sb = new StringBuilder();
		sb.append(e.getFieldErrors()
			.stream()
			.map(FieldError::getField)
			.collect(Collectors.joining(", "))
		);
		sb.append(ResponseMessage.INVALID_PARAM);
		return sb.toString();
	}

	@Override
	public String httpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error(e.getMessage());
		log.error(getStackTrace(e));
		return ResponseMessage.INVALID_MESSAGE;
	}

	private String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
