package com.socialreputation.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutUserActivityReview {
	private String activityId;
	private String reviewId;
	private String revieweeId;
	private String rating;

}
