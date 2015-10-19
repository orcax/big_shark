package com.prj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.common.util.DataWrapper;
import com.prj.common.util.DateUtils;
import com.prj.dao.ProjectDao;
import com.prj.dao.ReviewDao;
import com.prj.entity.Project;
import com.prj.entity.Project.Phase;
import com.prj.entity.Review;
import com.prj.service.ReviewService;

/**
 * The Class ReviewServiceImpl.
 */
@Service
public class ReviewServiceImpl extends BaseServiceImpl<Review, Long> implements
        ReviewService {

    /** The review dao. */
    ReviewDao reviewDao;

    /** The project dao. */
    @Autowired
    ProjectDao projectDao;

    /**
     * Instantiates a new review service impl.
     *
     * @param reviewDao the review dao
     */
    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao) {
        super(Review.class, reviewDao);
        this.reviewDao = reviewDao;
    }

    /** 
     * @see com.prj.service.BaseService#create(java.lang.Object)
     */
    public DataWrapper create(Review review) {
        return new DataWrapper(reviewDao.create(review));
    }

    /** 
     * @see com.prj.service.ReviewService#listByProject(java.lang.Long, java.util.Set)
     */
    public DataWrapper listByProject(Long projectId, Set<Phase> phases) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", projectId));
        List<Review> reviews = reviewDao.getAllByConditions(conditions);
        Map<Phase, List<Map<String, Object>>> data = new HashMap<Phase, List<Map<String, Object>>>();
        for (Phase p : phases) {
            data.put(p, new ArrayList<Map<String, Object>>());
        }
        for (Review r : reviews) {
            if (phases.contains(r.getPhase()))
                data.get(r.getPhase()).add(r.toSimpleFormat());
        }
        return new DataWrapper(data);
    }

    /** 
     * @see com.prj.service.ReviewService#addLike(com.prj.entity.Review)
     */
    public DataWrapper addLike(Review review) {
        // 1) Add review to database
        Review newData = review.simpleClone();
        newData.setPhase(Phase.AUDITION);
        newData.setLiked(review.getLiked());
        Long id = reviewDao.create(newData);

        // 2) Change project phase if there are more than 2 like
        Project project = projectDao.read(review.getProjectId());
        if (project.getPhase() != Phase.AUDITION) {
            return new DataWrapper(id);
        }
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", review.getProjectId()));
        conditions.add(Restrictions.eq("phase", Phase.AUDITION));
        conditions.add(Restrictions.eq("liked", true));
        int count = reviewDao.countByConditions(conditions);
        if (count >= 2) {
            project.setPhase(Phase.FIRST_REVIEW);
            project.setModifyTime(DateUtils.getCurrentTimestamp());
            projectDao.update(project);
        }

        return new DataWrapper(id);
    }

    /** 
     * @see com.prj.service.ReviewService#addFirstReview(com.prj.entity.Review)
     */
    public DataWrapper addFirstReview(Review review) {
        Review newData = review.simpleClone();
        newData.setPhase(Phase.FIRST_REVIEW);
        newData.setContent(review.getContent());
        newData.setInclined(review.getInclined());
        return new DataWrapper(reviewDao.create(newData));
    }

    /** 
     * @see com.prj.service.ReviewService#addFinalReview(com.prj.entity.Review)
     */
    public DataWrapper addFinalReview(Review review) {
        Review newData = review.simpleClone();
        newData.setPhase(Phase.FINAL_REVIEW);
        newData.setContent(review.getContent());
        newData.setInclined(review.getInclined());
        if (review.getInclined()) {
            newData.setAssessedValue(review.getAssessedValue());
            newData.setInvestAmount(review.getInvestAmount());
            newData.setInvested(review.getInvested());
        }
        return new DataWrapper(reviewDao.create(newData));
    }

    /** 
     * @see com.prj.service.ReviewService#addEvaluation(com.prj.entity.Review)
     */
    public DataWrapper addEvaluation(Review review) {
        Review newData = review.simpleClone();
        newData.setPhase(Phase.ACCEPTED);
        newData.setManagerName(review.getManagerName());
        newData.setContent(review.getContent());
        return new DataWrapper(reviewDao.create(newData));
    }

    /** 
     * @see com.prj.service.impl.BaseServiceImpl#update(java.io.Serializable, java.lang.Object)
     */
    @Override
    public DataWrapper update(Long reviewId, Review newData) {
        Review review = reviewDao.read(reviewId);
        if (review.getPhase() == Phase.AUDITION) {
            review.setLiked(newData.getLiked());
        } else if (review.getPhase() == Phase.FIRST_REVIEW) {
            review.setManagerName(newData.getManagerName());
            review.setContent(newData.getContent());
            review.setInclined(newData.getInclined());
        } else if (review.getPhase() == Phase.FINAL_REVIEW) {
            review.setManagerName(newData.getManagerName());
            review.setContent(newData.getContent());
            review.setInclined(newData.getInclined());
            if (review.getInclined()) {
                review.setAssessedValue(newData.getAssessedValue());
                review.setInvestAmount(newData.getInvestAmount());
                review.setInvested(newData.getInvested());
            }
        } else if (review.getPhase() == Phase.ACCEPTED) {
            review.setManagerName(newData.getManagerName());
            review.setContent(newData.getContent());
        }
        review.setModifyTime(DateUtils.getCurrentTimestamp());
        reviewDao.update(review);
        return DataWrapper.voidSuccessRet;
    }

}
