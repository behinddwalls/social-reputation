package com.socialreputation.api.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetUserActivity extends PutUserActivity {
	private String rating;
	private UserAudit audit;
	private String createrId;
	private List<GetUserActivityReview> reviews;

	@Builder
	public GetUserActivity(final String activityId, final String subject, final String description,
			final List<PutParticipant> participants, final String rating, final UserAudit audit, final String createrId,
			final List<GetUserActivityReview> reviews) {
		super(activityId, subject, description, participants);
		this.rating = rating;
		this.audit = audit;
		this.createrId = createrId;
		this.reviews = reviews;
	}

}
