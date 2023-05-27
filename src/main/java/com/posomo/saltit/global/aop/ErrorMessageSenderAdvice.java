package com.posomo.saltit.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ErrorMessageSenderAdvice {

	public Object sendError(ProceedingJoinPoint joinPoint) throws Throwable;
}
