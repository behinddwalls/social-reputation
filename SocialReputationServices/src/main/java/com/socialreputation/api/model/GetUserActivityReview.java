package com.socialreputation.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetUserActivityReview extends PutUserActivityReview {
	private String reviewerId;
	private UserAudit audit;

	@Builder
	public GetUserActivityReview(final String activityId, final String reviewId, final String revieweeId,
			final String rating, final String reviewerId, final UserAudit audit) {
		super(activityId, reviewId, revieweeId, rating);
		this.reviewerId = revieweeId;
		this.audit = audit;
	}
}
