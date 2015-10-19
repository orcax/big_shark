package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.CompanyDao;
import com.prj.entity.Company;

@Repository("CompanyDaoImpl")
public class CompanyDaoImpl extends BaseDaoHibernateImpl<Company, Long>
        implements CompanyDao {
    
    public CompanyDaoImpl() {
        super(Company.class);
    }

}
