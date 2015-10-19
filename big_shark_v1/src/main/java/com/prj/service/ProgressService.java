package com.prj.service;

import com.prj.common.util.DataWrapper;
import com.prj.entity.Progress;

public interface ProgressService extends BaseService<Progress, Long> {
    
    boolean isValid(Long accountId, Long projectId);
    
    DataWrapper listByProject(Long projectId);

}
