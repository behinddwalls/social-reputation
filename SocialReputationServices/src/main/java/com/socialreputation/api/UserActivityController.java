package com.socialreputation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.socialreputation.api.model.GetUserActivity;
import com.socialreputation.api.model.GetUserActivityReview;
import com.socialreputation.api.model.PutUserActivity;
import com.socialreputation.api.model.PutUserActivityReview;
import com.socialreputation.handler.ActivityHandler;
import com.socialreputation.util.URLUtility;

@RestController
@RequestMapping(value = "/activities")
public class UserActivityController extends AbstractController {

	@Autowired
	private ActivityHandler activityHandler;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addActivity(@RequestBody final PutUserActivity activity) {
		final PutUserActivity createdActivity = activityHandler.createUserActivity(activity);
		return ResponseEntity.created(URLUtility.fromString("/activities/" + createdActivity.getActivityId())).build();
	}

	@RequestMapping(value = "{activity-id}", method = RequestMethod.GET)
	public ResponseEntity<GetUserActivity> getActivity(@PathVariable("activity-id") final String activityId) {
		final GetUserActivity userActivity = activityHandler.getUserActivity(activityId);
		return ResponseEntity.ok(userActivity);
	}

	@RequestMapping(value = "{activity-id}", method = RequestMethod.PUT)
	public ResponseEntity<?> putActivity(@PathVariable("activity-id") final String activityId,
			@RequestBody final PutUserActivity putUserActivity) {
		putUserActivity.setActivityId(activityId);
		activityHandler.updateUserActivity(putUserActivity);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "{activity-id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteActivity(@PathVariable("activity-id") final String activityId) {
		activityHandler.deleteUserActivity(activityId);
		return ResponseEntity.noContent().build();
	}

	// @RequestMapping(value = "{activity-id}/participants/{participant-id}",
	// method = RequestMethod.PUT)
	// public ResponseEntity<?>
	// putParticipantToActivity(@PathVariable("activity-id") final String
	// activityId,
	// @PathVariable("participant-id") final String participantId,
	// @RequestBody final PutUserActivityParticipant userActivityParticipant) {
	// activityHandler.putUserActivityParticipant(userActivityParticipant);
	// return ResponseEntity.noContent().build();
	// }
	//
	// @RequestMapping(value = "{activity-id}/participants/{participant-id}",
	// method = RequestMethod.DELETE)
	// public ResponseEntity<?>
	// deleteParticipantToActivity(@PathVariable("activity-id") final String
	// activityId,
	// @PathVariable("participant-id") final String participantId) {
	// activityHandler.deleteUserActivityParticipant(activityId, participantId);
	// return ResponseEntity.noContent().build();
	// }

	@RequestMapping(value = "{activity-id}/reviews", method = RequestMethod.POST)
	public ResponseEntity<?> addReviewToActivity(@PathVariable("activity-id") final String activityId,
			@RequestBody final PutUserActivityReview userActivityReview) {
		final PutUserActivityReview review = activityHandler.createUserActivityReview(userActivityReview);
		return ResponseEntity
				.created(URLUtility.fromString("/activities/" + activityId + "/reviews/" + review.getReviewId()))
				.build();
	}

	@RequestMapping(value = "{activity-id}/reviews/{review-id}", method = RequestMethod.GET)
	public ResponseEntity<GetUserActivityReview> getReviewFromActivity(
			@PathVariable("activity-id") final String activityId, @PathVariable("review-id") final String reviewId) {
		final GetUserActivityReview review = activityHandler.getUserActivityReview(activityId, reviewId);
		return ResponseEntity.ok(review);
	}

	@RequestMapping(value = "{activity-id}/reviews/{review-id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteReviewFromActivity(@PathVariable("activity-id") final String activityId,
			@PathVariable("review-id") final String reviewId) {
		activityHandler.deleteUserActivityReview(activityId, reviewId);
		return ResponseEntity.noContent().build();
	}
}
