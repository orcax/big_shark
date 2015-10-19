package com.prj.service;

import com.prj.common.util.DataWrapper;
import com.prj.entity.Company;

public interface CompanyService extends BaseService<Company, Long> {
    
    DataWrapper readByProject(Long projectId);
    
}
