package com.socialreputation.validator.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.socialreputation.validator.AbstractDataValidator.ErrorInfo;

public class ValidationUtility {

	public static boolean isNull(Object object, String message, final ErrorInfo errorInfo) {
		if (object == null) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String object, String message, final ErrorInfo errorInfo) {
		if (StringUtils.isEmpty(object)) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	public static boolean isMinLength(String object, int minlength, String message, final ErrorInfo errorInfo) {
		if (StringUtils.isEmpty(object) || message.length() < minlength) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	public static boolean isMaxLength(String object, int maxlength, String message, final ErrorInfo errorInfo) {
		if (!StringUtils.isEmpty(object) && message.length() > maxlength) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	public static boolean isEmpty(List<?> object, String message, final ErrorInfo errorInfo) {
		if (object == null || object.isEmpty()) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	public static boolean isMinLength(List<?> object, int minlegth, String message, final ErrorInfo errorInfo) {
		if (object == null || object.isEmpty() || object.size() < minlegth) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	public static boolean isMaxLength(List<?> object, int maxlegth, String message, final ErrorInfo errorInfo) {
		if (object != null && object.size() > maxlegth) {
			addMessage(message, errorInfo);
			return true;
		}
		return false;
	}

	private static void addMessage(String message, ErrorInfo errorInfo) {
		errorInfo.addErrorMessage(message);
	}
}
