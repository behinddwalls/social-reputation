package com.socialreputation.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socialreputation.exception.InternalServerException;
import com.socialreputation.exception.ResourceAccessDeniedException;
import com.socialreputation.exception.ResourceBadRequestException;
import com.socialreputation.exception.ResourceConflictException;
import com.socialreputation.exception.ResourceNotFoundException;
import com.socialreputation.exception.ResourceUnauthorizedException;
import com.socialreputation.util.DateUtility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ServiceExceptionHandler {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Error {
		private long timestamp;
		private int status;
		private String error;
		private String exception;
		private String message;
		private String path;
	}

	private Error buildError(HttpServletRequest request, HttpServletResponse response, Exception ex,
			final HttpStatus status) {
		log.error("Logging error while gandling", ex);
		System.out.println(ex);
		return Error.builder().path(request.getRequestURI()).timestamp(DateUtility.nowAsMilis()).status(status.value())
				.error(status.getReasonPhrase()).exception(ex.getClass().getSimpleName()).message(ex.getMessage())
				.build();
	}

	@ExceptionHandler(ResourceAccessDeniedException.class)
	@ResponseBody
	public ResponseEntity<?> handleAccessDenied(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(buildError(request, response, ex, HttpStatus.FORBIDDEN));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	public ResponseEntity<?> handleNotFound(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(buildError(request, response, ex, HttpStatus.NOT_FOUND));
	}

	@ExceptionHandler(ResourceConflictException.class)
	@ResponseBody
	public ResponseEntity<?> handleConflict(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(request, response, ex, HttpStatus.CONFLICT));
	}

	@ExceptionHandler(ResourceBadRequestException.class)
	@ResponseBody
	public ResponseEntity<?> handleBadRequest(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(buildError(request, response, ex, HttpStatus.UNAUTHORIZED));
	}

	@ExceptionHandler(ResourceUnauthorizedException.class)
	@ResponseBody
	public ResponseEntity<?> handleUnauthorized(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(buildError(request, response, ex, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler({ Exception.class, Throwable.class, InternalServerException.class })
	@ResponseBody
	public ResponseEntity<?> handleInternalError(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(buildError(request, response, ex, HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
