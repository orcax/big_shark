package com.prj.service;

import com.prj.common.util.DataWrapper;
import com.prj.entity.Education;
import com.prj.entity.Founder;
import com.prj.entity.Work;

public interface FounderService extends BaseService<Founder, Long> {
    
    DataWrapper listByProject(Long projectId);
    
    Work getWork(Long workId);
    
    DataWrapper listWorkByFounder(Long founderId);
    
    DataWrapper createWork(Work work);
    
    DataWrapper updateWork(Long workId, Work work);
    
    DataWrapper deleteWork(Long workId);
    
    Education getEdu(Long eduId);
    
    DataWrapper listEduByFounder(Long founderId);
    
    DataWrapper createEdu(Education edu);
    
    DataWrapper updateEdu(Long eduId, Education edu);
    
    DataWrapper deleteEdu(Long eduId);
}
