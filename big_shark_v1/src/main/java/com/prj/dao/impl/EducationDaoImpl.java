package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.EducationDao;
import com.prj.entity.Education;

@Repository("EducationDaoImpl")
public class EducationDaoImpl extends BaseDaoHibernateImpl<Education, Long>
        implements EducationDao {

    public EducationDaoImpl() {
        super(Education.class);
    }

}
