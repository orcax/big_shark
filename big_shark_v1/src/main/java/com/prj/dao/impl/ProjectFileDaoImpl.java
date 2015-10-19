package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.ProjectFileDao;
import com.prj.entity.ProjectFile;

@Repository("AttachmentDaoImpl")
public class ProjectFileDaoImpl extends BaseDaoHibernateImpl<ProjectFile, Long>
        implements ProjectFileDao {
    
    public ProjectFileDaoImpl() {
        super(ProjectFile.class);
    }

}
