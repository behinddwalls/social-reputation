package com.socialreputation.model;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class AuthenticatedUser implements Authentication {

	private String name;
	private boolean authenticated = true;
	private String details;

	public AuthenticatedUser(final String userId, final String details) {
		this.name = userId;
		this.details = details;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public String getDetails() {
		return details;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean b) throws IllegalArgumentException {
		this.authenticated = b;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
