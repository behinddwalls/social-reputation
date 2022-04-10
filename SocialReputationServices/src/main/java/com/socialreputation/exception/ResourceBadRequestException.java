package com.socialreputation.exception;

public class ResourceBadRequestException extends RuntimeException {

	public ResourceBadRequestException() {
		super();
	}

	public ResourceBadRequestException(String message) {
		super(message);
	}

	public ResourceBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceBadRequestException(Throwable cause) {
		super(cause);
	}

	protected ResourceBadRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
