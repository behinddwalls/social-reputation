package com.socialreputation.oauth2;

import lombok.Getter;

public enum GrantType {
	FACEBOOK_TOKEN("facebook_token"), REFRESH_TOKEN("refresh_token");

	@Getter
	private final String type;

	private GrantType(final String type) {
		this.type = type;
	}
}
