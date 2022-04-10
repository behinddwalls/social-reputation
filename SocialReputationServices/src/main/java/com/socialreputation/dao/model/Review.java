package com.socialreputation.dao.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = Review.TABLE_NAME)
public class Review {
	@DynamoDBHashKey(attributeName = Fields.ACTIVITY_ID)
	private String activityId;
	@DynamoDBRangeKey(attributeName = Fields.REVIEW_ID)
	private String reviewId;
	@DynamoDBAttribute(attributeName = Fields.REVIEWER_ID)
	private String reviewerId;
	@DynamoDBAttribute(attributeName = Fields.REVIEWEE_ID)
	private String revieweeId;
	@DynamoDBAttribute(attributeName = Fields.RATING)
	private String rating;
	@DynamoDBAttribute(attributeName = Fields.COMMENTS)
	private String comments;
	@DynamoDBAttribute(attributeName = Fields.AUDIT)
	private Audit audit;

	/* Constants */
	public static final String TABLE_NAME = "Review";

	public static class Fields {
		public static final String ACTIVITY_ID = "activityId";
		public static final String REVIEW_ID = "reviewId";
		public static final String REVIEWEE_ID = "revieweeId";
		public static final String REVIEWER_ID = "reviewerId";
		public static final String RATING = "rating";
		public static final String COMMENTS = "comments";
		public static final String AUDIT = "audit";
	}
}
