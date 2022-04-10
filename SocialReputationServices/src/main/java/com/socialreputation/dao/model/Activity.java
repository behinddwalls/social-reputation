package com.socialreputation.dao.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.socialreputation.dao.model.Activity.Review.Status;
import com.socialreputation.dao.model.Relationship.Fields;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamoDBTable(tableName = Activity.TABLE_NAME)
public class Activity {
	@DynamoDBHashKey(attributeName = Fields.ACTIVITY_ID)
	private String activityId;
	@DynamoDBAttribute(attributeName = Fields.SUBJECT)
	private String subject;
	@DynamoDBAttribute(attributeName = Fields.DESCRIPTION)
	private String description;
	@DynamoDBAttribute(attributeName = Fields.RATING)
	private String rating;
	@DynamoDBAttribute(attributeName = Fields.CREATER_ID)
	private String creatorId;
	@DynamoDBAttribute(attributeName = Fields.AUDIT)
	private Audit audit;
	@DynamoDBAttribute(attributeName = Fields.PARTICIPANTS)
	private List<Participant> participants;
	@DynamoDBAttribute(attributeName = Fields.REVIEWS)
	private List<Review> reviews;
	@DynamoDBAttribute(attributeName = Fields.VERSION)
	private Long version;

	/* Constants */
	public static final String TABLE_NAME = "Activity";

	public static class Fields {
		public static final String ACTIVITY_ID = "activityId";
		public static final String SUBJECT = "subject";
		public static final String DESCRIPTION = "description";
		public static final String RATING = "rating";
		public static final String CREATER_ID = "creatorId";
		public static final String AUDIT = "audit";
		public static final String PARTICIPANTS = "participants";
		public static final String REVIEWS = "reviews";
		public static final String VERSION = "version";
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@DynamoDBDocument
	public static class Participant {
		@DynamoDBAttribute(attributeName = Fields.USER_ID)
		private String userId;
		@DynamoDBAttribute(attributeName = Fields.STATUS)
		@DynamoDBTypeConvertedEnum
		private Status status;
		@DynamoDBAttribute(attributeName = Fields.PARTITION_ID)
		private long partitionId;

		public static class Fields {
			public static final String USER_ID = "userId";
			public static final String STATUS = "status";
			public static final String PARTITION_ID = "partitionId";
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	@DynamoDBDocument
	public static class Review {
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

		public static class Fields {
			public static final String REVIEW_ID = "reviewId";
			public static final String REVIEWEE_ID = "revieweeId";
			public static final String REVIEWER_ID = "reviewerId";
			public static final String RATING = "rating";
			public static final String COMMENTS = "comments";
			public static final String STATUS = "status";
			public static final String AUDIT = "audit";
		}

		public static enum Status {
			PENDING, ACCEPTED, REJECTED;
		}
	}

}
