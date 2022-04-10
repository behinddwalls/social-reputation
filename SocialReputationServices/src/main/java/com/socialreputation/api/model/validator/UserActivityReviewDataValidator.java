package com.socialreputation.api.model.validator;

import org.springframework.stereotype.Component;

import com.socialreputation.api.model.PutUserActivityReview;
import com.socialreputation.validator.AbstractDataValidator;
import com.socialreputation.validator.util.ValidationUtility;

@Component
public class UserActivityReviewDataValidator extends AbstractDataValidator<PutUserActivityReview> {

	@Override
	protected void isValid(PutUserActivityReview data, ErrorInfo errorInfo, final String globalMessage) {

		if (!ValidationUtility.isNull(data, "Activity can not be null", errorInfo)) {
			ValidationUtility.isEmpty(data.getRevieweeId(), "Reviewee can not be empty", errorInfo);
			ValidationUtility.isEmpty(data.getRating(), "Rating is required", errorInfo);
		}
	}

	@Override
	public boolean supports(Class<PutUserActivityReview> clazz) {
		return PutUserActivityReview.class.isAssignableFrom(clazz);
	}

}
