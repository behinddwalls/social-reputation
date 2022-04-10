package com.socialreputation.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FbUserProfile {
	private String id;
	private String name;
	private String about;
	private String email;
	private String website;
	private String birthday;
	private String gender;
	private Location location;
	private Hometown hometown;

	@Data
	@NoArgsConstructor
	public static class Location {
		private String name;
		private String id;
	}

	@Data
	@NoArgsConstructor
	public static class Hometown {
		private String name;
		private String id;
	}
}
