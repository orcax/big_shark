package com.prj.service;

import java.util.Set;

import com.prj.common.util.DataWrapper;
import com.prj.entity.Project.Phase;
import com.prj.entity.Review;

public interface ReviewService extends BaseService<Review, Long> {

    DataWrapper listByProject(Long projectId, Set<Phase> phases);

    DataWrapper addLike(Review review);

    DataWrapper addFirstReview(Review review);

    DataWrapper addFinalReview(Review review);

    DataWrapper addEvaluation(Review review);

}
