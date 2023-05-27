package com.posomo.saltit.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.posomo.saltit.service.MessageSender;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackMessageAdvice implements ErrorMessageSenderAdvice {

	private final MessageSender messageSender;

	@Override
	@Around(value = "execution(public * com.posomo.saltit.global.aop.ControllerAdvice.*(..))")
	public Object sendError(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (args.length == 1 && args[0] instanceof Throwable exception) {
			StringBuilder stringBuilder = buildMessage(exception);

			messageSender.sendMessage(stringBuilder.toString());
		}
		return joinPoint.proceed();
	}

	@NotNull
	private static StringBuilder buildMessage(Throwable exception) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Transaction Id\n");
		stringBuilder.append(MDC.get("transactionId"));
		stringBuilder.append("\n\n");

		stringBuilder.append("Error Message\n");
		stringBuilder.append(exception.getMessage());
		stringBuilder.append("\n\n");

		stringBuilder.append("Stack Trace\n");
		for (StackTraceElement element : exception.getStackTrace()) {
			stringBuilder.append(element.toString());
			stringBuilder.append("\n");
		}
		return stringBuilder;
	}
}
