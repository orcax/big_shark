package com.prj.service;

import org.springframework.web.multipart.MultipartFile;

import com.prj.common.util.DataWrapper;
import com.prj.common.util.FileObject;
import com.prj.entity.Project;
import com.prj.entity.Project.InvestType;
import com.prj.entity.Project.Phase;
import com.prj.entity.Project.Status;

public interface ProjectService extends BaseService<Project, Long> {

    boolean isValid(Long accountId, Long projectId, Phase Phase);

    boolean isValid(Long accountId, Long projectId);

    boolean isValid(Long projectId, Phase phase);
    
    Project get(Long projectId);

    DataWrapper listByCreater(Long createrId);

    DataWrapper search(Phase phase, Status status, String city, String name,
            Boolean notified, InvestType investType, Integer pageSize,
            Integer pageNumber);

    DataWrapper create(Project project);

    DataWrapper update(Long projectId, Project project);

    /**
     * Submit project and enter first review.
     * 
     * @param projectId
     * @return
     */
    DataWrapper submit(Long projectId);

    FileObject readFile(Long fileId);

    DataWrapper listFiles(Long projectId);

    DataWrapper addFile(Long projectId, MultipartFile file);

    DataWrapper deleteFile(Long fileId);

    DataWrapper changePhase(Long projectId, Phase nextPhase,
            InvestType investType);

    DataWrapper batchNotify(long[] projectIds);

    DataWrapper changeStatus(Long projectId, Status status);
}
