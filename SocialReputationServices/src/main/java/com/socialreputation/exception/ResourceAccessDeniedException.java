package com.socialreputation.exception;

public class ResourceAccessDeniedException extends RuntimeException {

	public ResourceAccessDeniedException() {
		super();
	}

	public ResourceAccessDeniedException(String message) {
		super(message);
	}

	public ResourceAccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceAccessDeniedException(Throwable cause) {
		super(cause);
	}

	protected ResourceAccessDeniedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
