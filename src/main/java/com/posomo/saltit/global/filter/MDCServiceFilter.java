package com.posomo.saltit.global.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@Order(0)
@RequiredArgsConstructor
public class MDCServiceFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		MDC.put("transactionId", request.getHeader("X-Transaction-Id"));
		filterChain.doFilter(request, response);
		MDC.clear();
	}
}
