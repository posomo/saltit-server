package com.posomo.saltit.global.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.posomo.saltit.domain.exception.NoRecordException;

public interface ControllerAdvice {
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	String baseExceptionHandler(Exception e);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoRecordException.class)
	String noRecordExceptionHandler(NoRecordException e);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	String methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	String methodArgumentNotValidException(MethodArgumentNotValidException e);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	String httpMessageNotReadableException(HttpMessageNotReadableException e);
}
