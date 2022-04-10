package com.socialreputation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.socialreputation.handler.UserProfileHandler;
import com.socialreputation.service.AwsS3Service;

@RestController
@RequestMapping(value = "/search")
public class UserSearchController {

	@Autowired
	private AwsS3Service s3Handler;

	@Autowired
	private UserProfileHandler userProfileHandler;

	@RequestMapping(value = "{keyword:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> getActivity(@PathVariable("keyword") final String keyword) {

		return ResponseEntity.ok(userProfileHandler.searchUserByEmail(keyword));
	}

	@RequestMapping(value = "images", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
		System.out.println(file.getName());
		s3Handler.uploadMutltipartFile(file);
		return ResponseEntity.ok().build();
	}
}
