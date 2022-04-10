
package com.socialreputation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/feeds")
public class UserFeedController {

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getActivity(@RequestParam("limit") final int limit,
			@RequestParam("offset") final int offset) {

		return ResponseEntity.ok().build();
	}
}
