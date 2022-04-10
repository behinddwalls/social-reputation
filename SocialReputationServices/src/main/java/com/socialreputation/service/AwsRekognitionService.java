package com.socialreputation.service;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;

@Service
public class AwsRekognitionService {

	@Autowired
	private AmazonRekognitionClient rekognitionClient;

	public SearchFacesByImageResult searchFacesByImage(final String collectionId, final String s3Bukct,
			final String s3Key, final byte[] imageBytes, final float threshold, int maxFaces) {
		Image image = null;
		if (imageBytes != null && imageBytes.length != 0) {
			image = new Image().withBytes(ByteBuffer.wrap(imageBytes));
		} else if (StringUtils.isEmpty(s3Bukct) && StringUtils.isEmpty(s3Key)) {
			image = new Image().withS3Object(new S3Object().withBucket(s3Bukct).withName(s3Key));
		} else {
			throw new IllegalArgumentException("No image supplied for search");
		}
		SearchFacesByImageRequest request = new SearchFacesByImageRequest().withCollectionId(collectionId)
				.withFaceMatchThreshold(threshold).withMaxFaces(maxFaces).withImage(image);
		final SearchFacesByImageResult result = rekognitionClient.searchFacesByImage(request);
		if (result != null && result.getSdkHttpMetadata().getHttpStatusCode() == HttpStatus.OK.value()) {
			return result;
		} else {
			// handle exceptions or errors
			throw new RuntimeException("Failed to search face");
		}

	}

	public SearchFacesByImageResult searchFacesByImage(final String collectionId, final String s3Bukct,
			final String s3Key) {
		return searchFacesByImage(collectionId, s3Bukct, s3Key, null, 90F, 10);
	}

	public SearchFacesByImageResult searchFacesByImage(final String collectionId, final byte[] imageBytes) {
		return searchFacesByImage(collectionId, null, null, imageBytes, 90F, 10);
	}

}
