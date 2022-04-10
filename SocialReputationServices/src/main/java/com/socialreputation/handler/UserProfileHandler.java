package com.socialreputation.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.socialreputation.api.model.FbUserProfile;
import com.socialreputation.api.model.UserSearchResult;
import com.socialreputation.api.model.UserSearchResult.UserSearchResultBuilder;
import com.socialreputation.dao.UserProfileDao;
import com.socialreputation.dao.model.Audit;
import com.socialreputation.dao.model.UserProfile;
import com.socialreputation.dao.model.UserProfileByEmail;
import com.socialreputation.exception.ResourceNotFoundException;
import com.socialreputation.service.FacebookService;
import com.socialreputation.util.CommonUtility;
import com.socialreputation.util.DateUtility;

@Component
public class UserProfileHandler {

	@Autowired
	private UserProfileDao userProfileDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private FacebookService facebookService;

	public UserProfile get(final String userId) {
		return userProfileDao.read(userId);
	}

	public UserSearchResult searchUserByEmail(final String email) {
		UserSearchResultBuilder builder = UserSearchResult.builder();
		try {
			final UserProfileByEmail userProfileByEmail = userProfileDao.readByEmail(email);
			builder.userId(userProfileByEmail.getUserId()).exists(true);
		} catch (ResourceNotFoundException e) {
			// do nothing
			builder.exists(false);
		}
		return builder.email(email).build();
	}

	public UserProfileByEmail getOrCreateUserProfile(final String name, final List<String> emails,
			final List<String> phones) {

		UserProfileByEmail mainProfile = null;
		String mainEmail = null;
		String mainPhone = null;
		final List<UserProfileByEmail> profiles = new ArrayList<UserProfileByEmail>();
		if (emails != null) {
			for (String email : emails) {
				mainEmail = email;
				try {
					final UserProfileByEmail profile = userProfileDao.readByEmail(email);
					mainProfile = profile;
				} catch (ResourceNotFoundException e) {
					// not found create a profile from email
					if (mainProfile == null) {
						profiles.add(UserProfileByEmail.builder().email(email).build());
					}
				}
			}
		} else if (null != phones) {
			// create profile for all phones and map to single userId
			for (String phone : phones) {
				mainPhone = phone;
				try {
					final UserProfileByEmail profile = userProfileDao.readByEmail(phone);
					mainProfile = profile;
				} catch (ResourceNotFoundException e) {
					// not found create a profile from phone
					if (mainProfile == null) {
						profiles.add(UserProfileByEmail.builder().email(phone).build());
					}
				}
			}
		}
		if (mainProfile == null) {// create all the profiles for a new userId
			return registerUser(name, StringUtils.isEmpty(mainEmail) ? mainPhone : mainEmail);
		}
		// user already has a main selected profile don't create any
		return mainProfile;
	}

	private UserProfileByEmail registerUser(final String name, final String email) {

		final String currentTime = DateUtility.nowAsString();

		final UserProfileByEmail profileByEmail = UserProfileByEmail.builder().email(email)
				.hashedPassword(passwordEncoder.encode(CommonUtility.getUUID()))
				.audit(Audit.builder().createTime(currentTime).updateTime(currentTime).build())
				.userId(CommonUtility.getUUID()).build();

		userProfileDao.registerByEmail(profileByEmail);

		final UserProfile userProfile = UserProfile.builder().userId(profileByEmail.getUserId())
				.email(profileByEmail.getEmail()).name(name).audit(profileByEmail.getAudit()).build();

		userProfileDao.registerByUserId(userProfile);

		return profileByEmail;
	}

	public UserProfileByEmail verifyAndRegisterUser(final String token) {
		/*
		 * if (!facebookService.isValidAccessToken(token)) { throw new
		 * ResourceBadRequestException("Pass the valid facebook accessToken in header to signin."
		 * ); }
		 */
		final FbUserProfile fbProfile = facebookService.getUserProfile(token);

		try {
			final UserProfileByEmail profilebyEmail = userProfileDao.readByEmail(fbProfile.getEmail());
			return profilebyEmail;
		} catch (ResourceNotFoundException e) {
			// try registering the user
			return registerUser(fbProfile.getName(), fbProfile.getEmail());
		}
	}

}
