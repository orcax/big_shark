/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.exception.AppException;
import com.prj.common.util.DataWrapper;
import com.prj.config.security.AccountUserDetails;
import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.entity.Project;
import com.prj.entity.Project.Phase;
import com.prj.entity.Project.Status;
import com.prj.service.AccountService;
import com.prj.service.CompanyService;
import com.prj.service.EquityService;
import com.prj.service.FileService;
import com.prj.service.FounderService;
import com.prj.service.ProgressService;
import com.prj.service.ProjectService;
import com.prj.service.ReviewService;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: BaseController.java, v 0.1 Jun 21, 2015 11:01:25 AM yiliang.gyl
 *          Exp $
 */
@RequestMapping("/api")
public class BaseController {
    /**
     * for convince of dependency configuration change to full name and move to
     * inherited controller finally
     */
    @Autowired
    AccountService  as;

    @Autowired
    ProjectService  projectService;

    @Autowired
    FileService     fileService;

    @Autowired
    ReviewService   reviewService;

    @Autowired
    CompanyService  companyService;

    @Autowired
    ProgressService progressService;

    @Autowired
    FounderService  founderService;

    @Autowired
    EquityService   equityService;

    protected void assertOwner(long id) {
        Account a = currentAccount();
        if (id != a.getId()) {
            throw AppException.newInstanceOfForbiddenException(a.getName());
        }
    }

    protected boolean isAdmin() {
        return currentAccount().getRole() == Role.ADMINISTRATOR;
    }

    /**
     * Get current account fetch from token.
     * <p/>
     * Note that password property is already erased.
     *
     * @return current account logged in
     */
    protected Account currentAccount() {
        return ((AccountUserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getAccount();
    }

    protected DataWrapper isProjectValid(Long projectId, Phase phase) {
        return isProjectValid(projectId, phase, Status.NORMAL);
    }

    /**
     * 判断当前对项目的操作是否合法，要求如下：
     * 1）项目ID是否为空；
     * 2）项目记录是否存在；
     * 3）针对CUSTOMER角色，项目是否属于当前用户；
     * 4）项目阶段是否符合要求；
     * 5）项目状态是否符合要求；
     * 
     * @param projectId
     * @param phase
     * @return
     */
    protected DataWrapper isProjectValid(Long projectId, Phase phase, Status status) {
        if (projectId == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_INSUFFICIENT, "Project ID is missing.");
        }
        Project project = projectService.get(projectId);
        if (project == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND, String.format(
                "The project (ID=%d) does not exist.", projectId));
        }
        Account user = currentAccount();
        if (user.getRole() == Role.CUSTOMER && project.getCreaterId() != user.getId()) {
            return new DataWrapper(ErrorCodeEnum.UNAUTHORIZED, String.format(
                "Unauthorized to handle with the project (ID=%d).", projectId));
        }
        if (phase != null && project.getPhase() != phase) {
            return new DataWrapper(ErrorCodeEnum.BIZ_PHASE_ERROR, String.format(
                "Cannot perform this operation until the project is in phase %s.", phase));
        }
        if (status != null && project.getStatus() != status) {
            return new DataWrapper(ErrorCodeEnum.BIZ_STATUS_ERROR, String.format(
                "Cannot perfrom this operation until the project status is %s.", status));
        }
        return null;
    }

}
