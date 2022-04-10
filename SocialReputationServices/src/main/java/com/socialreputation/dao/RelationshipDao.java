package com.socialreputation.dao;

import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.socialreputation.dao.model.Relationship;
import com.socialreputation.exception.InternalServerException;
import com.socialreputation.exception.ResourceNotFoundException;

@Repository
public class RelationshipDao extends AbstractDao {

	@Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = { Exception.class }, exclude = {
			ResourceNotFoundException.class })
	public Relationship read(final String requesterId, final int partitionId) {
		final Relationship relationship = dynamoDBMapper.load(Relationship.class, requesterId, partitionId);
		if (null == relationship)
			throw new ResourceNotFoundException("Activities or reviews relationship not found");
		return relationship;
	}

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class }, exclude = {
	// ResourceNotFoundException.class })
	public Relationship read(final String requesterId) {
		final DynamoDBQueryExpression<Relationship> exp = new DynamoDBQueryExpression<Relationship>()
				.withScanIndexForward(false).withHashKeyValues(Relationship.builder().requesterId(requesterId).build())
				.withLimit(1);
		final PaginatedQueryList<Relationship> queryList = dynamoDBMapper.query(Relationship.class, exp);

		if (null == queryList || queryList.isEmpty())
			throw new ResourceNotFoundException("Activities or reviews relationship not found");
		return queryList.get(0);
	}

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class })
	public void write(Relationship relationship) {
		final DynamoDBSaveExpression exp = new DynamoDBSaveExpression();
		exp.withExpectedEntry(Relationship.Fields.PARTITION_ID, new ExpectedAttributeValue()
				.withValue(new AttributeValue().withN(String.valueOf(relationship.getPartitionId()))).withExists(true))
				.withConditionalOperator(ConditionalOperator.AND).withExpectedEntry(Relationship.Fields.VERSION,
						new ExpectedAttributeValue()
								.withValue(new AttributeValue().withN(String.valueOf(relationship.getVersion() - 1)))
								.withExists(true));
		dynamoDBMapper.save(relationship);
	}

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class })
	public void writeToNewPartition(Relationship relationship) {
//		final DynamoDBSaveExpression exp = new DynamoDBSaveExpression();
//		exp.withExpectedEntry(Relationship.Fields.REQUESTER_ID, new ExpectedAttributeValue()
//				.withValue(new AttributeValue().withN(String.valueOf(relationship.getPartitionId()))).withExists(false))
//				.withExpectedEntry(Relationship.Fields.PARTITION_ID,
//						new ExpectedAttributeValue()
//								.withValue(new AttributeValue().withN(String.valueOf(relationship.getPartitionId())))
//								.withExists(false));
		dynamoDBMapper.save(relationship);
	}

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class })
	// public void delete(Relationship relationship) {
	// final DynamoDBDeleteExpression deleteExpression = new
	// DynamoDBDeleteExpression();
	// deleteExpression.withExpectedEntry(Relationship.Fields.ACTIVITY_ID, new
	// ExpectedAttributeValue()
	// .withValue(new
	// AttributeValue().withS(relationship.getActivityId())).withExists(true));
	// dynamoDBMapper.delete(relationship);
	// }

	// @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000), value = {
	// Exception.class })
	public void batchCreate(final List<Relationship> relationships) {
		final List<FailedBatch> result = dynamoDBMapper.batchSave(relationships);
		if (result != null && !result.isEmpty()) {
			throw new InternalServerException("Failed to create the relationsip");
		}
	}

}
