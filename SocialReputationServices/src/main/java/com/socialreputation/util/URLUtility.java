package com.socialreputation.util;

import java.net.URI;
import java.net.URISyntaxException;

public final class URLUtility {

	public static URI fromString(final String uriString) {
		try {
			return new URI(uriString);
		} catch (URISyntaxException e) {
			throw new RuntimeException("URL malformed", e);
		}
	}
}
