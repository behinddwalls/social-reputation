package com.socialreputation.dao.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = Relationship.TABLE_NAME)
public class Relationship {
	@DynamoDBHashKey(attributeName = Fields.REQUESTER_ID)
	private String requesterId;
	@DynamoDBRangeKey(attributeName = Fields.PARTITION_ID)
	private long partitionId;
	@DynamoDBAttribute(attributeName = Fields.RELATIONS)
	private List<Relation> relations;
	@DynamoDBAttribute(attributeName = Fields.VERSION)
	private Long version;
	/* Constants */
	public static final String TABLE_NAME = "Relationship";

	public static class Fields {
		public static final String REQUESTER_ID = "requesterId";
		public static final String PARTITION_ID = "partitionId";
		public static final String RELATIONS = "relations";
		public static final String VERSION = "version";
	}

	public static enum RelationshipType {
		REVIWER, REVIWEE, CREATOR;
	}

	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@DynamoDBDocument
	@Data
	public static class Relation {
		//@DynamoDBAttribute(attributeName = Fields.RELATIONSHIP_TYPE)
		//private RelationshipType relationshipType;
		@DynamoDBAttribute(attributeName = Fields.ACCEPTER_ID)
		private String accepterId;
		@DynamoDBAttribute(attributeName = Fields.ACTIVITY_ID)
		private String activityId;
		@DynamoDBAttribute(attributeName = Fields.REVIEW_ID)
		private String reviewId;
		@DynamoDBAttribute(attributeName = Fields.TIMESTAMP)
		private long timestamp;

		public static class Fields {
			public static final String ACCEPTER_ID = "accepterId";
			public static final String RELATIONSHIP_TYPE = "relationshipType";
			public static final String ACTIVITY_ID = "activityId";
			public static final String REVIEW_ID = "reviewId";
			public static final String TIMESTAMP = "timestamp";
		}
	}
}
