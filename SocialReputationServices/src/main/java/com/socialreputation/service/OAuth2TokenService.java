package com.socialreputation.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialreputation.dao.model.UserProfileByEmail;
import com.socialreputation.exception.ResourceBadRequestException;
import com.socialreputation.handler.UserProfileHandler;
import com.socialreputation.model.AuthenticatedUser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class OAuth2TokenService {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserProfileHandler userProfileHandler;

	private long EXPIRATIONTIME = 1000 * 60 * 60; // 10 days
	private String ACCESS_TOKEN_SECRET = "ThisIsASecret";
	private String REFRESH_TOKEN_SECRET = "refreshThisIsASecret";
	private String tokenPrefix = "Bearer";
	private String ACCESS_TOKEN_HEADER = "x-authorization-access-token";
	private String REFRESH_TOKEN_HEADER = "x-authorization-refresh-token";
	private String AUTHORIZATION = "Authorization";

	public void addAuthentication(HttpServletResponse response, String token) {
		// get the fb access token
		if (StringUtils.isEmpty(token)) {
			throw new ResourceBadRequestException("Empty token passed");
		}

		final String cleanToken = token.replaceFirst(tokenPrefix, "").trim();
		final UserProfileByEmail profile = userProfileHandler.verifyAndRegisterUser(cleanToken);
		String userDetails;
		try {
			userDetails = objectMapper.writeValueAsString(profile);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("failed to parse" + profile);
		}
		// We generate a token now.
		String accessToken = getAccessToken(userDetails);
		String refreshToken = getRefreshToken(userDetails);

		response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
		response.addHeader(REFRESH_TOKEN_HEADER, refreshToken);
	}

	private String getAccessToken(final String details) {
		return Jwts.builder().setSubject(details).setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, ACCESS_TOKEN_SECRET).compact();
	}

	private String getRefreshToken(final String details) {
		return Jwts.builder().setSubject(details)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME * 60 * 24 * 30))
				.signWith(SignatureAlgorithm.HS512, REFRESH_TOKEN_SECRET).compact();
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		if (token != null) {
			// parse the token.
			final String cleanToken = token.replaceFirst(tokenPrefix, "").trim();
			try {
				System.out
						.println(Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET).parseClaimsJws(cleanToken).getBody());
				String details = Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET).parseClaimsJws(cleanToken).getBody()
						.getSubject();
				if (details != null) // we managed to retrieve a user
				{
					final UserProfileByEmail profileByEmail = objectMapper.readValue(details, UserProfileByEmail.class);
					return new AuthenticatedUser(profileByEmail.getUserId(), details);
				}
			} catch (IOException | ExpiredJwtException e) {
				return null;
			}
		}
		return null;
	}

	public void refreshAuthentication(HttpServletResponse response, final String refreshTokenHeaderValue) {
		final String cleanToken = refreshTokenHeaderValue.replaceFirst(tokenPrefix, "").trim();
		String details = Jwts.parser().setSigningKey(REFRESH_TOKEN_SECRET).parseClaimsJws(cleanToken).getBody()
				.getSubject();
		try {
			if (null != details) {
				response.addHeader(ACCESS_TOKEN_HEADER, getAccessToken(details));
			} else {
				throw new IllegalArgumentException("Invalid username");
			}
		} catch (ExpiredJwtException e) {
			throw e;
		}
	}
}