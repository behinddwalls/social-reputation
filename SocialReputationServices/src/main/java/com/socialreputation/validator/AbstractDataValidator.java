package com.socialreputation.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.socialreputation.exception.ResourceBadRequestException;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractDataValidator<T> {

	protected abstract void isValid(final T data, final ErrorInfo errorInfo, final String globalMessage);

	protected abstract boolean supports(final Class<T> clazz);

	public void validate(final T data) {
		validate(data, StringUtils.EMPTY);
	}

	public void validate(final T data, final String globalMessage) {

		final ErrorInfo error = new ErrorInfo();
		if (supports((Class<T>) data.getClass())) {
			isValid(data, error, globalMessage);
			if (!error.getErrorMessages().isEmpty())
				throw new ResourceBadRequestException(error.getErrorMessages().toString());
			return;
		} else {
			throw new ClassCastException("Invalid class assigment");
		}

	}

	public static class ErrorInfo {
		@Getter(AccessLevel.PACKAGE)
		final List<String> errorMessages;

		public ErrorInfo() {
			errorMessages = new ArrayList<String>();
		}

		public void addErrorMessage(final String message) {
			this.errorMessages.add(message);
		}
	}

}
