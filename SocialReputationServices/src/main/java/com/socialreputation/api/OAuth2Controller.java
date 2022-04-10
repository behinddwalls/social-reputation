package com.socialreputation.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialreputation.exception.ResourceBadRequestException;
import com.socialreputation.oauth2.GrantType;
import com.socialreputation.service.OAuth2TokenService;

@RestController
@RequestMapping(value = "/oauth2")
public class OAuth2Controller {

	@Autowired
	private OAuth2TokenService authenticationService;

	@RequestMapping(value = "token", method = RequestMethod.POST)
	public ResponseEntity<?> getRefreshToken(@RequestParam(name = "grant_type", required = true) final String grantType,
			@RequestHeader(name = "Authorization") final String token, final HttpServletResponse response) {
		GrantType grantTypeEnum = null;
		try {
			grantTypeEnum = GrantType.valueOf(grantType.toUpperCase());
		} catch (Exception e) {
			throw new ResourceBadRequestException("Invalid grant type");
		}
		switch (grantTypeEnum) {
		case FACEBOOK_TOKEN:
			authenticationService.addAuthentication(response, token);
			break;
		case REFRESH_TOKEN:
			authenticationService.refreshAuthentication(response, token);
			break;
		}

		return ResponseEntity.noContent().build();
	}
}
