package com.socialreputation.api.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistration {
	@NotNull
	private String email;
	@NotNull
	@Size(min = 6)
	private String password;
	@NotNull
	@Size(max = 50)
	private String firstName;
	private String lastName;

}
