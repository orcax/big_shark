package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.WorkDao;
import com.prj.entity.Work;

@Repository("WorkDaoImpl")
public class WorkDaoImpl extends BaseDaoHibernateImpl<Work, Long> implements
        WorkDao {

    public WorkDaoImpl() {
        super(Work.class);
    }

}
