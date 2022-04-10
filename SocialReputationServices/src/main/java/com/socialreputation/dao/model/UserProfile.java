package com.socialreputation.dao.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = UserProfile.TABLE_NAME)
public class UserProfile {
	@DynamoDBHashKey(attributeName = Fields.USER_ID)
	private String userId;
	@DynamoDBAttribute(attributeName = Fields.EMAIL)
	private String email;
	@DynamoDBAttribute(attributeName = Fields.NAME)
	private String name;
	@DynamoDBAttribute(attributeName = Fields.IMAGE_SIGNATURE)
	private String imageSignature;
	@DynamoDBAttribute(attributeName = Fields.IMAGE_URL)
	private String imageUrl;
	@DynamoDBAttribute(attributeName = Fields.SOCIAL_NETWORKS)
	private SocialNetworks socialNetworks;
	@DynamoDBAttribute(attributeName = Fields.AUDIT)
	private Audit audit;

	/* Constants */
	public static final String TABLE_NAME = "UserProfile";
	public static final String EMAIL_INDEX = "email-index";

	public static class Fields {
		public static final String USER_ID = "userId";
		public static final String AUDIT = "audit";
		public static final String EMAIL = "email";
		public static final String HASHED_PASSWORD = "hashedPassword";
		public static final String NAME = "name";
		public static final String IMAGE_SIGNATURE = "imageSigntature";
		public static final String IMAGE_URL = "imageUrl";
		public static final String SOCIAL_NETWORKS = "socialNetworks";
	}

	@Data
	@Builder
	@DynamoDBDocument
	public static class SocialNetworks {
		@DynamoDBAttribute(attributeName = Fields.FACEBOOK_ID)
		private String facebookId;
		@DynamoDBAttribute(attributeName = Fields.LINKEDIN_ID)
		private String linkedInId;

		public static class Fields {
			private static final String FACEBOOK_ID = "facebookId";
			private static final String LINKEDIN_ID = "linkedInId";
		}
	}

}
