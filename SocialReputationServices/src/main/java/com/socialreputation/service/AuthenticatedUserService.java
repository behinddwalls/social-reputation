package com.socialreputation.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

	public String getUserId() {
		final Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		return authenticatedUser.getName();
	}
}
