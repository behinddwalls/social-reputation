package com.socialreputation.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.socialreputation.constant.AwsConstant;
import com.socialreputation.exception.ResourceBadRequestException;
import com.socialreputation.util.CommonUtility;

@Service
public class AwsS3Service {
	@Autowired
	private AmazonS3Client s3Client;
	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	@Autowired
	@Qualifier(AwsConstant.AWS_S3_BUCKET_USER_IMAGES)
	private String imagesBucket;

	private String getActivitiesS3Key() {
		return String.format("%s/%s/%s", authenticatedUserService.getUserId(), "activities", CommonUtility.getUUID());
	}

	public String uploadMutltipartFile(final MultipartFile file) {
		byte[] fileBytes;
		try {
			fileBytes = file.getBytes();

			final InputStream stream = new ByteArrayInputStream(fileBytes);
			final ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(fileBytes.length);
			final PutObjectRequest request = new PutObjectRequest(imagesBucket, getActivitiesS3Key(), stream, metadata);
			s3Client.putObject(request);
			return "";
		} catch (IOException e) {
			throw new ResourceBadRequestException("Failed to upload file");
		}
	}
}
