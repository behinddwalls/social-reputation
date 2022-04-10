package com.socialreputation.handler;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.socialreputation.dao.RelationshipDao;
import com.socialreputation.dao.model.Relationship;
import com.socialreputation.dao.model.Relationship.Relation;
import com.socialreputation.dao.model.Relationship.RelationshipType;
import com.socialreputation.exception.ResourceNotFoundException;
import com.socialreputation.util.DateUtility;

@Component
public class RelationshipHandler {

	@Autowired
	private RelationshipDao relationshipDao;

	Relationship createRelationshipForActivity(final String activityId, final String requesterId) {
		/*
		 * This should be called after validation from ActivityHandler
		 */
		Relationship relationship = null;
		boolean firstTime = false;
		long partitionId = -1;
		try {
			relationship = relationshipDao.read(requesterId);
			partitionId = relationship.getPartitionId();
		} catch (ResourceNotFoundException e) {
			// very first time
			firstTime = true;
		}
		final Relation relation = Relation.builder().accepterId(requesterId).activityId(activityId)
				.timestamp(DateUtility.nowAsMilis()).build();

		if ((relationship != null && relationship.getRelations().size() >= 1) || firstTime) {
			relationship = Relationship.builder().requesterId(requesterId).relations(new ArrayList<Relation>())
					.version(1L).partitionId(partitionId + 1).build();
			relationship.getRelations().add(relation);
			relationshipDao.writeToNewPartition(relationship);
		} else {
			relationship.getRelations().add(relation);
			relationship.setVersion(relationship.getVersion() + 1);
			relationshipDao.write(relationship);
		}

		return relationship;
	}

	void deleteRelationshipForActivity(final String activityId) {

	}

}
