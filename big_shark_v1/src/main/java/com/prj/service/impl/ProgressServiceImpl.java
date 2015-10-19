package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.common.util.DataWrapper;
import com.prj.common.util.DateUtils;
import com.prj.dao.ProgressDao;
import com.prj.dao.ProjectDao;
import com.prj.entity.Progress;
import com.prj.entity.Project;
import com.prj.service.ProgressService;

/**
 * The Class ProgressServiceImpl.
 */
@Service
public class ProgressServiceImpl extends BaseServiceImpl<Progress, Long>
        implements ProgressService {

    /** The progress dao. */
    ProgressDao progressDao;

    /** The project dao. */
    @Autowired
    ProjectDao projectDao;

    /**
     * Instantiates a new progress service impl.
     *
     * @param progressDao the progress dao
     */
    @Autowired
    public ProgressServiceImpl(ProgressDao progressDao) {
        super(Progress.class, progressDao);
        this.progressDao = progressDao;
    }

    /** 
     * @see com.prj.service.ProgressService#isValid(java.lang.Long, java.lang.Long)
     */
    public boolean isValid(Long accountId, Long projectId) {
        Project project = projectDao.read(projectId);
        return project != null && accountId == project.getCreaterId();
    }

    /** 
     * @see com.prj.service.ProgressService#listByProject(java.lang.Long)
     */
    public DataWrapper listByProject(Long projectId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", projectId));
        return new DataWrapper(progressDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.BaseService#create(java.lang.Object)
     */
    public DataWrapper create(Progress progress) {
        return new DataWrapper(progressDao.create(progress));
    }

    /** 
     * @see com.prj.service.impl.BaseServiceImpl#update(java.io.Serializable, java.lang.Object)
     */
    @Override
    public DataWrapper update(Long progressId, Progress progress) {
        Progress p = progressDao.read(progressId);
        p.setDescription(progress.getDescription());
        p.setEventType(progress.getEventType());
        p.setModifyTime(DateUtils.getCurrentTimestamp());
        progressDao.update(p);
        return DataWrapper.voidSuccessRet;
    }

}
