package com.socialreputation.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.socialreputation.api.model.GetUserActivity;
import com.socialreputation.api.model.GetUserActivityReview;
import com.socialreputation.api.model.PutUserActivity;
import com.socialreputation.api.model.PutUserActivity.PutParticipant;
import com.socialreputation.api.model.PutUserActivityReview;
import com.socialreputation.api.model.UserAudit;
import com.socialreputation.api.model.validator.UserActivityDataValidator;
import com.socialreputation.api.model.validator.UserActivityReviewDataValidator;
import com.socialreputation.dao.ActivityDao;
import com.socialreputation.dao.model.Activity;
import com.socialreputation.dao.model.Activity.Participant;
import com.socialreputation.dao.model.Activity.Review;
import com.socialreputation.dao.model.Activity.Review.Status;
import com.socialreputation.dao.model.Audit;
import com.socialreputation.dao.model.Relationship;
import com.socialreputation.dao.model.UserProfileByEmail;
import com.socialreputation.exception.ResourceAccessDeniedException;
import com.socialreputation.exception.ResourceBadRequestException;
import com.socialreputation.exception.ResourceNotFoundException;
import com.socialreputation.service.AuthenticatedUserService;
import com.socialreputation.util.CommonUtility;
import com.socialreputation.util.DateUtility;
import com.socialreputation.validator.StringNotEmptyValidator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ActivityHandler {

	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	@Autowired
	private UserActivityDataValidator userActivityDataValidator;
	@Autowired
	private UserActivityReviewDataValidator userActivityReviewDataValidator;
	@Autowired
	private StringNotEmptyValidator stringNotEmptyValidator;
	@Autowired
	private RelationshipHandler relationshipHandler;
	@Autowired
	private UserProfileHandler userProfileHandler;

	public Activity getActivity(final String activityId) {
		return activityDao.read(activityId);
	}

	private void validateCreatorAccess(final Activity activity, final String message) {
		validateCreatorAccess(activity, message, authenticatedUserService.getUserId());
	}

	private void validateCreatorAccess(final Activity activity, final String message, final String userId) {
		if (!(activity.getCreatorId().equals(userId))) {
			throw new ResourceAccessDeniedException(message);
		}
	}

	private void validateParticipantsAccess(final Activity activity, final String message) {
		validateParticipantsAccess(activity, message, authenticatedUserService.getUserId());
	}

	private void validateParticipantsAccess(final Activity activity, final String message, final String userId) {
		if (!(getParticipants(activity.getParticipants()).getParticipantIds().contains(userId))) {
			throw new ResourceAccessDeniedException(message);
		}
	}

	private void validateAccess(final Activity activity, final String message) {
		validateAccess(activity, message, authenticatedUserService.getUserId());
	}

	private void validateAccess(final Activity activity, final String message, final String userId) {
		validateCreatorAccess(activity, message, userId);
		validateParticipantsAccess(activity, message, userId);
	}

	public GetUserActivity getUserActivity(final String activityId) {
		stringNotEmptyValidator.validate(activityId, "activityId can not be empty");
		final Activity activity = getActivity(activityId);
		validateAccess(activity, "User not allowed to access the resource");

		final String userId = authenticatedUserService.getUserId();

		// filter reviews for the authenticated user
		final List<GetUserActivityReview> userActivityReviews = new ArrayList<GetUserActivityReview>();
		Optional.ofNullable(activity.getReviews()).ifPresent(ars -> ars.forEach(ar -> {
			if (ar.getRevieweeId().equals(userId) || ar.getReviewerId().equals(userId)) {
				userActivityReviews.add(GetUserActivityReview.builder().reviewId(ar.getReviewId())
						.rating(ar.getRating()).revieweeId(ar.getRevieweeId()).reviewerId(ar.getReviewerId()).build());
			}
		}));

		return GetUserActivity.builder().rating(activity.getRating()).subject(activity.getSubject())
				.participants(getParticipants(activity.getParticipants()).getPutParticipants()).activityId(activityId)
				.createrId(activity.getCreatorId())
				.audit(UserAudit.builder().createTime(activity.getAudit().getCreateTime()).build())
				.reviews(userActivityReviews).description(activity.getDescription()).build();
	}

	public PutUserActivity createUserActivity(final PutUserActivity userActivity) {

		userActivityDataValidator.validate(userActivity);

		final String currentTime = DateUtility.nowAsString();
		final String activityId = generateUUID();
		final String userId = authenticatedUserService.getUserId();
		// create relations
		final Relationship relationship = relationshipHandler.createRelationshipForActivity(activityId, userId);

		userActivity.getParticipants().remove(userId);

		final Activity activity = Activity.builder().activityId(generateUUID()).subject(userActivity.getSubject())
				.description(userActivity.getDescription())
				.audit(Audit.builder().createTime(currentTime).updateTime(currentTime).build())
				.participants(createParticipants(userActivity.getParticipants())).creatorId(userId).rating("0")
				.version(1L).build();
		// add creator to participant list
		activity.getParticipants().add(createParticipant(userId, relationship.getPartitionId()));

		activityDao.create(activity);

		// TODO: Implement fallback for UUID collision

		userActivity.setActivityId(activity.getActivityId());
		return userActivity;
	}

	public PutUserActivity updateUserActivity(final PutUserActivity userActivity) {
		userActivityDataValidator.validate(userActivity);
		final Activity activity = getActivity(userActivity.getActivityId());
		validateCreatorAccess(activity, "User is not allowed to update");

		activity.getAudit().setUpdateTime(DateUtility.nowAsString());
		activity.setSubject(userActivity.getSubject());
		activity.setDescription(userActivity.getDescription());

		final List<String> participants = getParticipants(activity.getParticipants()).getParticipantIds();
		// TODO: filter the users w/o userIds in update and then try get or
		// create them

		final List<String> existingParticipantIds = new ArrayList<>();
		final List<PutParticipant> participantsWithoutUserId = new ArrayList<>();
		final List<Participant> newParticipants = new ArrayList<>();

		userActivity.getParticipants().forEach(p -> {
			if (!StringUtils.isEmpty(p.getUserId())) {
				existingParticipantIds.add(p.getUserId());
				// check if userId exists already otherwise fail with bad
				// request exception
				if (!participants.contains(p.getUserId())) {
					throw new ResourceBadRequestException("Participant with userId" + p.getUserId()
							+ " is new and can not be added. Please provide name, email or phone to add a participant to activity");
				}
			} else {
				participantsWithoutUserId.add(p);
				// if no userId, then get or create participants
				final List<PutParticipant> getOrCreateList = new ArrayList<>();
				getOrCreateList.add(p);
				final Participant participant = createParticipants(getOrCreateList).get(0);

				if (!participants.contains(participant.getUserId())) {
					// don't modify original participants yet
					newParticipants.add(participant);
				} else {
					existingParticipantIds.add(participant.getUserId());
				}
			}
		});
		System.out.println(existingParticipantIds);
		System.out.println(participants);
		// removed participants

		final Map<String, Participant> participantMap = new HashMap<>();
		final Map<String, Review> reviewMap = new HashMap<>();
		activity.getParticipants().forEach(p -> participantMap.put(p.getUserId(), p));
		Optional.ofNullable(activity.getReviews())
				.ifPresent(ars -> ars.forEach(ar -> reviewMap.put(ar.getReviewId(), ar)));

		final List<Participant> participantsCopy = new ArrayList<>(activity.getParticipants());
		final List<Review> reviewsCopy = activity.getReviews() == null ? null : new ArrayList<>(activity.getReviews());

		participantsCopy.forEach(p -> {
			// TODO: concurrent modification error

			if (!authenticatedUserService.getUserId().equals(p.getUserId())
					&& !existingParticipantIds.contains(p.getUserId())) {
				activity.getParticipants().remove(p);
				Optional.ofNullable(reviewsCopy).ifPresent(ars -> {
					ars.forEach(r -> {
						if (r.getRevieweeId().equals(p.getUserId()) || r.getReviewerId().equals(p.getUserId())) {
							activity.getReviews().remove(r);
						}
					});
				});
			}
		});
		activity.getParticipants().addAll(newParticipants);

		activity.setVersion(activity.getVersion() + 1);
		activityDao.write(activity);
		return userActivity;
	}

	public PutUserActivityReview createUserActivityReview(final PutUserActivityReview putUserActivityReview) {
		userActivityReviewDataValidator.validate(putUserActivityReview);

		final Activity activity = getActivity(putUserActivityReview.getActivityId());

		validateParticipantsAccess(activity,
				"User not allowed to create review for activityId : " + activity.getActivityId());
		validateParticipantsAccess(activity, "Reviewee not the part of this activity",
				putUserActivityReview.getRevieweeId());

		final String userId = authenticatedUserService.getUserId();
		// create a relation first
		final Map<String, Participant> participantMap = getParticipants(activity.getParticipants())
				.getUserIdToParticipant();
		if (participantMap.get(putUserActivityReview.getRevieweeId()).getPartitionId() == -1) {
			final Relationship relationship = relationshipHandler
					.createRelationshipForActivity(activity.getActivityId(), putUserActivityReview.getRevieweeId());
			participantMap.get(putUserActivityReview.getRevieweeId()).setPartitionId(relationship.getPartitionId());
		}
		if (participantMap.get(userId).getPartitionId() == -1) {
			final Relationship relationship = relationshipHandler
					.createRelationshipForActivity(activity.getActivityId(), userId);
			participantMap.get(userId).setPartitionId(relationship.getPartitionId());
		}

		final String currentTime = DateUtility.nowAsString();
		final Review review = Review.builder().reviewId(generateUUID()).reviewerId(authenticatedUserService.getUserId())
				.revieweeId(putUserActivityReview.getRevieweeId()).rating(putUserActivityReview.getRating())
				.audit(Audit.builder().createTime(currentTime).updateTime(currentTime).build()).build();
		final List<Review> reviews = Optional.ofNullable(activity.getReviews()).orElse(new ArrayList<Review>());
		reviews.add(review);
		activity.setReviews(reviews);
		activity.setRating("0");
		activity.getAudit().setUpdateTime(currentTime);
		activity.setVersion(activity.getVersion() + 1);
		activityDao.write(activity);

		putUserActivityReview.setReviewId(review.getReviewId());
		return putUserActivityReview;
	}

	public GetUserActivityReview getUserActivityReview(final String activityId, final String reviewId) {
		stringNotEmptyValidator.validate(activityId, "activityId can not be empty");
		stringNotEmptyValidator.validate(reviewId, "reviewId can not be empty");

		final Activity activity = getActivity(activityId);
		validateAccess(activity, "User not allowed to access reviewId: " + reviewId);

		final String userId = authenticatedUserService.getUserId();

		for (final Review review : activity.getReviews()) {
			if (review.getReviewId().equals(reviewId)) {
				if (userId.equals(review.getReviewerId()) || userId.equals(review.getRevieweeId())) {
					return GetUserActivityReview.builder().activityId(activityId).reviewId(reviewId)
							.revieweeId(review.getRevieweeId()).reviewerId(review.getReviewerId())
							.rating(review.getRating())
							.audit(UserAudit.builder().createTime(review.getAudit().getCreateTime()).build()).build();
				} else {
					throw new ResourceAccessDeniedException("User can not access reviewId: " + reviewId);
				}
			}
		}
		throw new ResourceNotFoundException("Review not found for reviewId: " + reviewId);
	}

	public void deleteUserActivity(final String activityId) {
		stringNotEmptyValidator.validate(activityId, "activityId can not be empty");
		final Activity activity = getActivity(activityId);
		validateCreatorAccess(activity, "Activity can only be deleted by creator.");
		// TODO: test this
		relationshipHandler.deleteRelationshipForActivity(activityId);
		// TODO: delete relations
		activityDao.remove(
				Activity.builder().activityId(activityId).creatorId(authenticatedUserService.getUserId()).build());

	}

	public void deleteUserActivityReview(final String activityId, final String reviewId) {
		stringNotEmptyValidator.validate(activityId, "activityId can not be empty");
		stringNotEmptyValidator.validate(reviewId, "reviewId can not be empty");

		final Activity activity = getActivity(activityId);
		validateAccess(activity, "User not allowed to delete reviewId:" + reviewId);

		final List<Review> reviews = activity.getReviews();
		for (final Review r : reviews) {
			if (r.getReviewId().equals(reviewId)) {
				if (r.getReviewerId().equals(authenticatedUserService.getUserId())) {
					reviews.remove(r);
					break;
				} else {
					throw new ResourceAccessDeniedException("User not allowed to delete the reviewId:" + reviewId);
				}
			}
		}
		activity.setReviews(reviews);
		activity.setRating("0");
		activity.getAudit().setUpdateTime(DateUtility.nowAsString());
		activity.setVersion(activity.getVersion() + 1);

		activityDao.write(activity);
	}

	// public void putUserActivityParticipant(final PutUserActivityParticipant
	// userReviewStatus) {
	// // TODO: data validation
	// final Activity activity = getActivity(userReviewStatus.getActivityId());
	//
	// final List<String> participants =
	// getParticipants(activity.getParticipants()).getParticipantIds();
	//
	// if (activity.getCreatorId().equals(authenticatedUserService.getUserId()))
	// {
	// if (!participants.contains(userReviewStatus.getParticipantId())) {
	// //TODO: change the participant logic here
	// activity.getParticipants().add(createParticipant(userReviewStatus.getParticipantId(),
	// -1));
	// } else {
	// throw new ResourceConflictException(
	// "Actvity already contains participant:" +
	// userReviewStatus.getParticipantId());
	// }
	// } else if (participants.contains(userReviewStatus.getParticipantId())
	// &&
	// authenticatedUserService.getUserId().equals(userReviewStatus.getParticipantId()))
	// {
	// // participants updating status
	// activity.getParticipants().stream().filter(p ->
	// p.getUserId().equals(userReviewStatus.getParticipantId()))
	// .forEach(fp -> {
	// if (fp.getStatus().equals(Status.PENDING)) {
	// fp.setStatus(Status.valueOf(userReviewStatus.getStatus().toUpperCase()));
	// } else {
	// throw new ResourceConflictException(
	// "Status is at the terminal status. Cannot be reveretd.");
	// }
	// });
	// } else {
	// throw new ResourceAccessDeniedException("User not allowed to access the
	// resource");
	// }
	// activity.setVersion(activity.getVersion() + 1);
	// activityDao.write(activity);
	//
	// }
	//
	// public void deleteUserActivityParticipant(final String activityId, final
	// String participantId) {
	// stringNotEmptyValidator.validate(activityId, "activityId can not be
	// empty");
	// stringNotEmptyValidator.validate(participantId, "participantId can not be
	// empty");
	//
	// final Activity activity = getActivity(activityId);
	// validateAccess(activity, "Only creator of the activity can delete
	// participants");
	//
	// boolean notFound = true;
	// int size = activity.getParticipants().size();
	// for (final Participant participant : activity.getParticipants()) {
	// if (participant.getUserId().equals(participantId)) {
	// if (size == 1) {
	// throw new ResourceConflictException("Atleast 1 participant is required.
	// Cannot delete.");
	// }
	// activity.getParticipants().remove(participant);
	// // delete reviews too
	//
	// notFound = false;
	// break;
	// }
	// }
	// if (notFound) {
	// throw new ResourceNotFoundException("Participant doesn't exist");
	// }
	//
	// activity.getAudit().setCreateTime(DateUtility.nowAsString());
	// activityDao.write(activity);
	// }

	private String generateUUID() {
		return CommonUtility.getUUID();
	}

	@AllArgsConstructor
	@Data
	public static class ParticipantsForProcessing {
		private Map<String, Participant> userIdToParticipant;
		private List<String> participantIds;
		private List<PutParticipant> putParticipants;
	}

	private ParticipantsForProcessing getParticipants(final List<Participant> participants) {
		final Map<String, Participant> participantMap = new HashMap<String, Participant>();
		final List<String> participantIds = new ArrayList<String>();
		final List<PutParticipant> putParticipants = new ArrayList<PutParticipant>();
		participants.forEach(p -> {
			participantMap.put(p.getUserId(), p);
			participantIds.add(p.getUserId());
			putParticipants.add(PutParticipant.builder().userId(p.getUserId()).build());
		});

		return new ParticipantsForProcessing(participantMap, participantIds, putParticipants);
	}

	private List<Participant> createParticipants(final List<PutParticipant> participantStrings) {
		final List<Participant> participants = new ArrayList<Participant>();
		// check if user exists then just add else create a new user then add
		participantStrings.forEach(p -> {
			final UserProfileByEmail profile = userProfileHandler.getOrCreateUserProfile(p.getName(), p.getEmails(),
					p.getPhones());
			participants.add(new Participant(profile.getUserId(), Status.PENDING, -1));
		});
		return participants;
	}

	private Participant createParticipant(final String participantId, final long partitionId) {
		return new Participant(participantId, Status.PENDING, partitionId);
	}
}
