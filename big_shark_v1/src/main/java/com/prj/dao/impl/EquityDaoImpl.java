package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.EquityDao;
import com.prj.entity.Equity;

@Repository("EquityDaoImpl")
public class EquityDaoImpl extends BaseDaoHibernateImpl<Equity, Long> implements
        EquityDao {

    public EquityDaoImpl() {
        super(Equity.class);
    }

}
