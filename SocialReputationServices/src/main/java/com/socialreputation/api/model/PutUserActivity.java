package com.socialreputation.api.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutUserActivity {

	private String activityId;
	private String subject;
	private String description;
	private List<PutParticipant> participants;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class PutParticipant {
		private String userId;
		private String name;
		private List<String> emails;
		private List<String> phones;
	}
}
