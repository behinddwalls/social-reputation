package com.socialreputation.exception;

public class ResourceConflictException extends RuntimeException {

	public ResourceConflictException() {
		super();
	}

	public ResourceConflictException(String message) {
		super(message);
	}

	public ResourceConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceConflictException(Throwable cause) {
		super(cause);
	}

	protected ResourceConflictException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
