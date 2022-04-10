package com.socialreputation.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class AbstractDao {

	@Autowired
	protected DynamoDBMapper dynamoDBMapper;

	@Autowired
	protected DynamoDB dynamoDB;
}
