package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.DateUtils;
import com.prj.dao.CompanyDao;
import com.prj.dao.ProjectDao;
import com.prj.entity.Company;
import com.prj.service.CompanyService;

/**
 * The Class CompanyServiceImpl.
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl<Company, Long>
        implements CompanyService {

    CompanyDao companyDao;

    @Autowired
    ProjectDao projectDao;

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao) {
        super(Company.class, companyDao);
        this.companyDao = companyDao;
    }

    public DataWrapper readByProject(Long projectId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", projectId));
        List<Company> companies = companyDao.getAllByConditions(conditions);
        if (companies == null || companies.size() <= 0) {
            return new DataWrapper(null);
        }
        return new DataWrapper(companies.get(0));
    }

    public DataWrapper create(Company company) {
        // 1) Check if company already exists
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", company.getProjectId()));
        if (companyDao.countByConditions(conditions) > 0) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DUPLICATIVE,
                    "Company info already exists.");
        }

        // 2) Create company info
        return new DataWrapper(companyDao.create(company));
    }

    @Override
    public DataWrapper update(Long companyId, Company company) {
        Company data = companyDao.read(companyId);
        data.setLegalEmail(company.getLegalEmail());
        data.setLegalMobile(company.getLegalMobile());
        data.setLegalPerson(company.getLegalPerson());
        data.setModifyTime(DateUtils.getCurrentTimestamp());
        data.setName(company.getName());
        data.setOfficeAddress(company.getOfficeAddress());
        data.setRegisterAddress(company.getRegisterAddress());
        data.setRegisterCapital(company.getRegisterCapital());
        data.setRegisterTime(company.getRegisterTime());
        data.setRegisterType(company.getRegisterType());
        companyDao.update(data);
        return DataWrapper.voidSuccessRet;
    }

}
