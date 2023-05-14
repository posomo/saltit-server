package com.posomo.saltit.global.filter;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {


	private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
		MediaType.valueOf("text/*"),
		MediaType.APPLICATION_FORM_URLENCODED,
		MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XML,
		MediaType.valueOf("application/*+json"),
		MediaType.valueOf("application/*+xml"),
		MediaType.MULTIPART_FORM_DATA
	);

	/**
	 * List of HTTP headers whose values should not be logged.
	 */
	private static final List<String> SENSITIVE_HEADERS = Arrays.asList(
		"authorization",
		"proxy-authorization"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (isAsyncDispatch(request)) {
			filterChain.doFilter(request, response);
		} else {
			doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
		}
	}

	protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {

		StringBuilder msg = new StringBuilder();

		try {
			beforeRequest(request, msg);
			filterChain.doFilter(request, response);
		}
		finally {
			afterRequest(request, response, msg);
			if(log.isInfoEnabled()) {
				log.info(msg.toString());
			}
			response.copyBodyToResponse();
		}
	}

	protected void beforeRequest(ContentCachingRequestWrapper request, StringBuilder msg) {
		if (log.isInfoEnabled()) {
			msg.append("\n-- REQUEST --\n");
			logRequestHeader(request, "", msg);
		}
	}

	protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, StringBuilder msg) {
		if (log.isInfoEnabled()) {
			logRequestBody(request, "", msg);
			msg.append("\n-- RESPONSE --\n");
			logResponse(response, "", msg);
		}
	}

	private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix, StringBuilder msg) {
		String queryString = request.getQueryString();
		if (queryString == null) {
			msg.append(String.format("%s %s %s", prefix, request.getMethod(), request.getRequestURI())).append("\n");
		} else {
			msg.append(String.format("%s %s %s?%s", prefix, request.getMethod(), request.getRequestURI(), queryString)).append("\n");
		}
		Collections.list(request.getHeaderNames())
			.forEach(headerName ->
				Collections.list(request.getHeaders(headerName))
					.forEach(headerValue -> {
						if(isSensitiveHeader(headerName)) {
							msg.append(String.format("%s %s: %s", prefix, headerName, "*******")).append("\n");
						}
						else {
							msg.append(String.format("%s %s: %s", prefix, headerName, headerValue)).append("\n");
						}
					}));
		msg.append(prefix).append("\n");
	}

	private static void logRequestBody(ContentCachingRequestWrapper request, String prefix, StringBuilder msg) {
		byte[] content = request.getContentAsByteArray();
		if (content.length > 0) {
			logContent(content, request.getContentType(), prefix, msg);
		}
	}

	private static void logResponse(ContentCachingResponseWrapper response, String prefix, StringBuilder msg) {
		int status = response.getStatus();
		msg.append(String.format("%s %s %s", prefix, status, HttpStatus.valueOf(status).getReasonPhrase())).append("\n");
		response.getHeaderNames()
			.forEach(headerName ->
				response.getHeaders(headerName)
					.forEach(headerValue ->
					{
						if(isSensitiveHeader(headerName)) {
							msg.append(String.format("%s %s: %s", prefix, headerName, "*******")).append("\n");
						}
						else {
							msg.append(String.format("%s %s: %s", prefix, headerName, headerValue)).append("\n");
						}
					}));
		msg.append(prefix).append("\n");
		byte[] content = response.getContentAsByteArray();
		if (content.length > 0) {
			logContent(content, response.getContentType(), prefix, msg);
		}
	}

	private static void logContent(byte[] content, String contentType, String prefix, StringBuilder msg) {
		MediaType mediaType = MediaType.valueOf(contentType);
		boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
		if (visible) {
			String contentString = new String(content, StandardCharsets.UTF_8);
			Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> msg.append(prefix).append(" ").append(line).append("\n"));
		} else {
			msg.append(String.format("%s [%d bytes content]", prefix, content.length)).append("\n");
		}
	}

	/**
	 * Determine if a given header name should have its value logged.
	 * @param headerName HTTP header name.
	 * @return True if the header is sensitive (i.e. its value should <b>not</b> be logged).
	 */
	private static boolean isSensitiveHeader(String headerName) {
		return SENSITIVE_HEADERS.contains(headerName.toLowerCase());
	}

	private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
		if (request instanceof ContentCachingRequestWrapper) {
			return (ContentCachingRequestWrapper) request;
		} else {
			return new ContentCachingRequestWrapper(request);
		}
	}

	private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
		if (response instanceof ContentCachingResponseWrapper) {
			return (ContentCachingResponseWrapper) response;
		} else {
			return new ContentCachingResponseWrapper(response);
		}
	}
}
