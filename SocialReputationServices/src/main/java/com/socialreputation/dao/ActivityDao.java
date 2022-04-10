package com.socialreputation.dao;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.socialreputation.dao.model.Activity;
import com.socialreputation.exception.ResourceNotFoundException;

@Repository
public class ActivityDao extends AbstractDao {

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class }, exclude = {
	// ResourceNotFoundException.class })
	public Activity read(final String activityId) {
		final Activity activity = dynamoDBMapper.load(Activity.class, activityId);
		if (activity == null)
			throw new ResourceNotFoundException("Activity doesn't exists for activityId:" + activityId);
		return activity;
	}

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class })
	public void create(final Activity activity) {
		final DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		saveExpression.withExpectedEntry(Activity.Fields.ACTIVITY_ID, new ExpectedAttributeValue().withExists(false));
		dynamoDBMapper.save(activity, saveExpression);
	}

	@Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = { Exception.class })
	public void write(final Activity activity) {
		final DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		saveExpression
				.withExpectedEntry(Activity.Fields.CREATER_ID,
						new ExpectedAttributeValue().withValue(new AttributeValue().withS(activity.getCreatorId()))
								.withExists(true))
				.withExpectedEntry(Activity.Fields.VERSION,
						new ExpectedAttributeValue()
								.withValue(new AttributeValue().withN(String.valueOf(activity.getVersion() - 1)))
								.withExists(true));
		dynamoDBMapper.save(activity, saveExpression);
	}

	@Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = { Exception.class })
	public void remove(final Activity activity) {
		final DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression();
		deleteExpression.withExpectedEntry(Activity.Fields.CREATER_ID, new ExpectedAttributeValue()
				.withValue(new AttributeValue().withS(activity.getCreatorId())).withExists(true));
		dynamoDBMapper.delete(Activity.builder().activityId(activity.getActivityId()).build());
	}

}
