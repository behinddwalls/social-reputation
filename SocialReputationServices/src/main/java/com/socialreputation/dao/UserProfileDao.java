package com.socialreputation.dao;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.socialreputation.dao.model.UserProfile;
import com.socialreputation.dao.model.UserProfileByEmail;
import com.socialreputation.exception.ResourceNotFoundException;

@Repository
public class UserProfileDao extends AbstractDao {

	@Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = { Exception.class }, exclude = {
			ResourceNotFoundException.class })
	public UserProfile read(final String userId) {
		final UserProfile userProfile = dynamoDBMapper.load(UserProfile.class, userId);
		if (null == userProfile) {
			throw new ResourceNotFoundException("User doesn't exists");
		}
		return userProfile;
	}

	@Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = { Exception.class }, exclude = {
			ResourceNotFoundException.class })
	public UserProfileByEmail readByEmail(final String email) {
		final UserProfileByEmail userProfileByEmail = dynamoDBMapper.load(UserProfileByEmail.class, email);

		if (null == userProfileByEmail) {
			throw new ResourceNotFoundException("User doesn't exists");
		}
		return userProfileByEmail;
	}

	public void write(final UserProfile userProfile) {
		dynamoDBMapper.save(userProfile);
	}

	public void registerByEmail(final UserProfileByEmail userProfileByEmail) {
		final DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();

		saveExpression.withExpectedEntry(UserProfile.Fields.EMAIL, new ExpectedAttributeValue().withExists(false));
		dynamoDBMapper.save(userProfileByEmail, saveExpression);
	}

	public void registerByUserId(final UserProfile userProfile) {
		final DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();

		saveExpression.withExpectedEntry(UserProfile.Fields.USER_ID, new ExpectedAttributeValue().withExists(false));
		dynamoDBMapper.save(userProfile, saveExpression);
	}
}
