package com.socialreputation.api.model.validator;

import org.springframework.stereotype.Component;

import com.socialreputation.api.model.PutUserActivity;
import com.socialreputation.validator.AbstractDataValidator;
import com.socialreputation.validator.util.ValidationUtility;

@Component
public class UserActivityDataValidator extends AbstractDataValidator<PutUserActivity> {

	@Override
	protected void isValid(PutUserActivity data, ErrorInfo errorInfo, final String globalMessage) {

		if (!ValidationUtility.isNull(data, "Activity can not be null", errorInfo)) {
			ValidationUtility.isEmpty(data.getSubject(), "Subject can not be empty", errorInfo);
			ValidationUtility.isMinLength(data.getParticipants(), 1, "Minimum of 1 participant required.", errorInfo);
		}
	}

	@Override
	public boolean supports(Class<PutUserActivity> clazz) {
		return PutUserActivity.class.isAssignableFrom(clazz);
	}

}
