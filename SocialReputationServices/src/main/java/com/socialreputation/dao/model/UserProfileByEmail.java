package com.socialreputation.dao.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.socialreputation.dao.model.UserProfile.Fields;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = UserProfileByEmail.TABLE_NAME)
public class UserProfileByEmail {

	@DynamoDBHashKey(attributeName = Fields.EMAIL)
	private String email;
	@DynamoDBAttribute(attributeName = Fields.HASHED_PASSWORD)
	private String hashedPassword;
	@DynamoDBAttribute(attributeName = Fields.USER_ID)
	private String userId;
	@DynamoDBAttribute(attributeName = Fields.AUDIT)
	private Audit audit;

	public static final String TABLE_NAME = "UserProfileByEmail";

}
