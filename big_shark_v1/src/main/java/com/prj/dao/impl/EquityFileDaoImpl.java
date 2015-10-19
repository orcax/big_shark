package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.EquityFileDao;
import com.prj.entity.EquityFile;

@Repository("EquityFileDaoImpl")
public class EquityFileDaoImpl extends BaseDaoHibernateImpl<EquityFile, Long>
        implements EquityFileDao {
    
    public EquityFileDaoImpl() {
        super(EquityFile.class);
    }

}
