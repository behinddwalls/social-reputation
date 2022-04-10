package com.socialreputation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.socialreputation.constant.AwsConstant;

@Configuration
public class AwsConfiguration {
	@Autowired
	private Environment environment;

	@Bean(AwsConstant.AWS_ACCESS_KEY_ID)
	public String getAwsAccessId() {
		return environment.getProperty(AwsConstant.AWS_ACCESS_KEY_ID);
	}

	@Bean(AwsConstant.AWS_SECRET_KEY)
	public String getAwsSecretKey() {
		return environment.getProperty(AwsConstant.AWS_SECRET_KEY);
	}

	@Bean(AwsConstant.AWS_REGION)
	public Regions getAwsRegion() {
		return Regions.valueOf(environment.getProperty(AwsConstant.AWS_REGION));
	}

	@Bean(AwsConstant.AWS_S3_BUCKET_USER_IMAGES)
	public String getUserImagesBucket() {
		return environment.getProperty(AwsConstant.AWS_S3_BUCKET_USER_IMAGES);
	}

	@Bean
	public BasicAWSCredentials getAwsCredentials() {
		return new BasicAWSCredentials(getAwsAccessId(), getAwsSecretKey());
	}

	@Bean
	public AmazonDynamoDBClient geAmazonDynamoDBClient(final BasicAWSCredentials credentials) {
		final AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
		client.withRegion(getAwsRegion());
		return client;
	}

	@Bean
	public DynamoDBMapper getDynamoDbMapper(final AmazonDynamoDBClient client) {
		return new DynamoDBMapper(client);
	}

	@Bean
	public DynamoDB getDynamo(final AmazonDynamoDBClient client) {
		return new DynamoDB(client);
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AmazonS3Client getAmazonS3Client(final BasicAWSCredentials credentials) {
		final AmazonS3Client client = new AmazonS3Client(credentials);
		client.withRegion(getAwsRegion());
		return client;
	}

	@Bean
	public AmazonRekognitionClient getAmazonRekognitionClient(final BasicAWSCredentials credentials) {
		final AmazonRekognitionClient client = new AmazonRekognitionClient(credentials);
		client.withRegion(getAwsRegion());
		return client;
	}
}
