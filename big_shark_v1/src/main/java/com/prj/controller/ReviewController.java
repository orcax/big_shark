package com.prj.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.RequiredRole;
import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.entity.Project.Phase;
import com.prj.entity.Review;

/**
 * The Class ReviewController.
 */
@RestController
public class ReviewController extends BaseController {

    /**
     * Checks if is review valid.
     *
     * @param rid the rid
     * @return the data wrapper
     */
    private DataWrapper isReviewValid(Long rid) {
        Review review = reviewService.get(rid);
        if (review == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND, String.format(
                "The review (ID=%d) does not exist.", rid));
        }
        return isProjectValid(review.getProjectId(), review.getPhase());
    }

    /**
     * 列出项目的评审信息（仅支持海选、初审、终审）。
     *
     * @param projectId the project id
     * @param phases the phases
     * @return the data wrapper
     */
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    DataWrapper listReviews(@RequestParam long projectId,
                            @RequestParam(required = false) Phase[] phases) {
        DataWrapper check = isProjectValid(projectId, null, null);
        if (check != null) {
            return check;
        }
        Set<Phase> p = new HashSet<Phase>();
        if (phases == null) {
            p.add(Phase.AUDITION);
            p.add(Phase.FIRST_REVIEW);
            p.add(Phase.FINAL_REVIEW);
        } else {
            p.addAll(Arrays.asList(phases));
            p.remove(Phase.ACCEPTED);
        }
        return reviewService.listByProject(projectId, p);
    }

    /**
     * 列出正式项目的评估信息。role = [MANAGER]
     *
     * @param projectId the project id
     * @return the data wrapper
     */
    @RequestMapping(value = "/review/evaluate", method = RequestMethod.GET)
    @RequiredRole(Role.MANAGER)
    DataWrapper listEvaluation(@RequestParam long projectId) {
        Set<Phase> phases = new HashSet<Phase>();
        phases.add(Phase.ACCEPTED);
        return reviewService.listByProject(projectId, phases);
    }

    /**
     * 添加评审信息（支持海选、初审、终审、正式入选）。role = [MANAGER]
     *
     * @param review the review
     * @return the data wrapper
     */
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @RequiredRole(Role.MANAGER)
    DataWrapper add(@RequestBody Review review) {
        if (review.getPhase() == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_INSUFFICIENT, "Review phase is missing.");
        }
        DataWrapper check = isProjectValid(review.getProjectId(), review.getPhase());
        if (check != null) {
            return check;
        }

        Account manager = currentAccount();
        review.setCreaterId(manager.getId());
        if (review.getPhase() == Phase.AUDITION) {
            review.setManagerName(manager.getName());
            return reviewService.addLike(review);
        }
        if (review.getPhase() == Phase.FIRST_REVIEW) {
            return reviewService.addFirstReview(review);
        }
        if (review.getPhase() == Phase.FINAL_REVIEW) {
            return reviewService.addFinalReview(review);
        }
        if (review.getPhase() == Phase.ACCEPTED) {
            return reviewService.addEvaluation(review);
        }
        return new DataWrapper(ErrorCodeEnum.BIZ_PHASE_ERROR, "Unsupported phase for review.");
    }

    /**
     * 更新评审信息。role = [MANAGER]
     *
     * @param rid the rid
     * @param review the review
     * @return the data wrapper
     */
    @RequestMapping(value = "/review/{rid}", method = RequestMethod.PUT)
    @RequiredRole(Role.MANAGER)
    DataWrapper edit(@PathVariable long rid, @RequestBody Review review) {
        DataWrapper check = isReviewValid(rid);
        return check != null ? check : reviewService.update(rid, review);
    }

}
