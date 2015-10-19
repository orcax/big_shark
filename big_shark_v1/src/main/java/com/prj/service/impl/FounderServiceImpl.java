package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.common.util.DataWrapper;
import com.prj.common.util.DateUtils;
import com.prj.dao.EducationDao;
import com.prj.dao.FounderDao;
import com.prj.dao.WorkDao;
import com.prj.entity.Education;
import com.prj.entity.Founder;
import com.prj.entity.Work;
import com.prj.service.FounderService;

/**
 * The Class FounderServiceImpl.
 */
@Service
public class FounderServiceImpl extends BaseServiceImpl<Founder, Long>
        implements FounderService {

    /** The founder dao. */
    FounderDao founderDao;

    /** The work dao. */
    @Autowired
    WorkDao workDao;

    /** The edu dao. */
    @Autowired
    EducationDao eduDao;

    /**
     * Instantiates a new founder service impl.
     *
     * @param founderDao the founder dao
     */
    @Autowired
    public FounderServiceImpl(FounderDao founderDao) {
        super(Founder.class, founderDao);
        this.founderDao = founderDao;
    }

    /** 
     * @see com.prj.service.FounderService#listByProject(java.lang.Long)
     */
    public DataWrapper listByProject(Long projectId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", projectId));
        return new DataWrapper(founderDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.BaseService#create(java.lang.Object)
     */
    public DataWrapper create(Founder founder) {
        return new DataWrapper(founderDao.create(founder));
    }

    /** 
     * @see com.prj.service.impl.BaseServiceImpl#update(java.io.Serializable, java.lang.Object)
     */
    @Override
    public DataWrapper update(Long founderId, Founder founder) {
        Founder data = founderDao.read(founderId);
        data.setEmail(founder.getEmail());
        data.setIdNumber(founder.getIdNumber());
        data.setMobile(founder.getMobile());
        data.setModifyTime(DateUtils.getCurrentTimestamp());
        data.setName(founder.getName());
        data.setPosition(founder.getPosition());
        data.setWechat(founder.getWechat());
        founderDao.update(data);
        return DataWrapper.voidSuccessRet;
    }

    @Override
    public DataWrapper delete(Long founderId) {
        Founder founder = founderDao.read(founderId);
        if (founder == null) {
            return DataWrapper.voidSuccessRet;
        }
        // 1) Delete related work info
        List<Criterion> c1 = new ArrayList<Criterion>();
        c1.add(Restrictions.eq("founderId", founderId));
        List<Work> works = workDao.getAllByConditions(c1);
        for (Work w : works) {
            workDao.delete(w);
        }

        // 2) Delete related education info
        List<Criterion> c2 = new ArrayList<Criterion>();
        c2.add(Restrictions.eq("founderId", founderId));
        List<Education> edus = eduDao.getAllByConditions(c2);
        for (Education e : edus) {
            eduDao.delete(e);
        }

        // 3) Delete founder info
        founderDao.delete(founder);

        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.FounderService#getWork(java.lang.Long)
     */
    public Work getWork(Long workId) {
        return workDao.read(workId);
    }

    /** 
     * @see com.prj.service.FounderService#listWorkByFounder(java.lang.Long)
     */
    public DataWrapper listWorkByFounder(Long founderId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("founderId", founderId));
        return new DataWrapper(workDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.FounderService#createWork(com.prj.entity.Work)
     */
    public DataWrapper createWork(Work work) {
        return new DataWrapper(workDao.create(work));
    }

    /** 
     * @see com.prj.service.FounderService#updateWork(java.lang.Long, com.prj.entity.Work)
     */
    public DataWrapper updateWork(Long workId, Work work) {
        Work data = workDao.read(workId);
        data.setCompany(work.getCompany());
        data.setEndDate(work.getEndDate());
        data.setModifyTime(DateUtils.getCurrentTimestamp());
        data.setPosition(work.getPosition());
        data.setStartDate(work.getStartDate());
        workDao.update(data);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.FounderService#deleteWork(java.lang.Long)
     */
    public DataWrapper deleteWork(Long workId) {
        Work work = workDao.read(workId);
        workDao.delete(work);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.FounderService#getEdu(java.lang.Long)
     */
    public Education getEdu(Long eduId) {
        return eduDao.read(eduId);
    }

    /** 
     * @see com.prj.service.FounderService#listEduByFounder(java.lang.Long)
     */
    public DataWrapper listEduByFounder(Long founderId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("founderId", founderId));
        return new DataWrapper(eduDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.FounderService#createEdu(com.prj.entity.Education)
     */
    public DataWrapper createEdu(Education edu) {
        return new DataWrapper(eduDao.create(edu));
    }

    /** 
     * @see com.prj.service.FounderService#updateEdu(java.lang.Long, com.prj.entity.Education)
     */
    public DataWrapper updateEdu(Long eduId, Education edu) {
        Education data = eduDao.read(eduId);
        data.setCollege(edu.getCollege());
        data.setEndDate(edu.getEndDate());
        data.setMajor(edu.getMajor());
        data.setModifyTime(DateUtils.getCurrentTimestamp());
        data.setStartDate(edu.getStartDate());
        eduDao.update(data);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.FounderService#deleteEdu(java.lang.Long)
     */
    public DataWrapper deleteEdu(Long eduId) {
        Education edu = eduDao.read(eduId);
        eduDao.delete(edu);
        return DataWrapper.voidSuccessRet;
    }

}
