package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.ProgressDao;
import com.prj.entity.Progress;

@Repository("ProgressDaoImpl")
public class ProgressDaoImpl extends BaseDaoHibernateImpl<Progress, Long>
        implements ProgressDao {
    
    public ProgressDaoImpl() {
        super(Progress.class);
    }

}
