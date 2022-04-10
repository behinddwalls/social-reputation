package com.socialreputation.exception;

public class ResourceUnauthorizedException extends RuntimeException {

	public ResourceUnauthorizedException() {
		super();
	}

	public ResourceUnauthorizedException(String message) {
		super(message);
	}

	public ResourceUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceUnauthorizedException(Throwable cause) {
		super(cause);
	}

	protected ResourceUnauthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
