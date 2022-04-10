package com.socialreputation.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.socialreputation.validator.util.ValidationUtility;

@Component
public class StringNotEmptyValidator extends AbstractDataValidator<String> {

	@Override
	protected void isValid(String data, com.socialreputation.validator.AbstractDataValidator.ErrorInfo errorInfo,
			String globalMessage) {
		final String errorMessage = StringUtils.isEmpty(globalMessage) ? "Value can not be empty" : globalMessage;
		ValidationUtility.isEmpty(data, errorMessage, errorInfo);
	}

	@Override
	protected boolean supports(Class<String> clazz) {
		return String.class.isAssignableFrom(clazz);
	}

}
