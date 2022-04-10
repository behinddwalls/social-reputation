package com.socialreputation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.socialreputation.dao.model.UserProfile;
import com.socialreputation.handler.UserProfileHandler;
import com.socialreputation.service.FacebookService;

@RestController
@RequestMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class UserProfileController extends AbstractController {

	@Autowired
	private UserProfileHandler userProfileHandler;
	@Autowired
	private FacebookService fbHandler;

	@RequestMapping(value = "{userId}", method = { RequestMethod.GET })
	public ResponseEntity<UserProfile> getUser(@PathVariable("userId") String userId) {
		final UserProfile userProfile = userProfileHandler.get(userId);
		return ResponseEntity.<UserProfile>ok(userProfile);
	}

	@RequestMapping(value = "test")
	public String test() {
		return "Hello World";
	}

}
