package com.socialreputation.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.amazonaws.services.machinelearning.model.InternalServerException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.socialreputation.api.model.FbUserProfile;
import com.socialreputation.service.HttpService.HttpRequest;
import com.socialreputation.service.HttpService.HttpRequest.HttpParams;
import com.socialreputation.validator.StringNotEmptyValidator;

@Component
public class FacebookService {

	@Autowired
	private HttpService httpService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private StringNotEmptyValidator stringNotEmptyValidator;

	private static final String FB_API_ENDPOINT = "https://graph.facebook.com/v2.8";

	public boolean isValidAccessToken(final String accessToken) {
		// decrypt token later
		stringNotEmptyValidator.validate(accessToken, "Provide the facebook access token in the header");

		final HttpRequest request = HttpRequest.builder().httpUrl(FB_API_ENDPOINT + "/debug_token")
				.queryParams(HttpParams.builder().with("input_token", accessToken)
						.with("access_token", "614520868700157|8xYC6pp3aOGMzxsqOEhmiSiRNak").build())
				.build();
		try {
			final HttpResponse response = httpService.executeGet(request);
			final String jsonReposneData = CharStreams
					.toString(new InputStreamReader(response.getEntity().getContent()));
			if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				final JsonNode node = objectMapper.readTree(jsonReposneData);
				System.out.println(jsonReposneData);
				return node.get("data").get("is_valid").asBoolean();
			} else {
				System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(jsonReposneData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new InternalServerException("Failed to validate token");
	}

	public FbUserProfile getUserProfile(final String accessToken) {
		final HttpRequest request = HttpRequest.builder().httpUrl(FB_API_ENDPOINT + "/me")
				.queryParams(HttpParams.builder()
						.with("fields", "about,email,website,birthday,hometown,gender,location,name")
						.with("access_token", accessToken).build())
				.build();

		try {
			final HttpResponse response = httpService.executeGet(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				final FbUserProfile profile = objectMapper.readValue(response.getEntity().getContent(),
						FbUserProfile.class);
				return profile;
			} else {
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new InternalServerException("Failed to get profile from fb");
	}
}
