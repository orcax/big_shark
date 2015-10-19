package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.FounderDao;
import com.prj.entity.Founder;

@Repository("FounderDaoImpl")
public class FounderDaoImpl extends BaseDaoHibernateImpl<Founder, Long>
        implements FounderDao {

    public FounderDaoImpl() {
        super(Founder.class);
    }

}
