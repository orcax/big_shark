package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.common.util.DateUtils;
import com.prj.dao.ProjectDao;
import com.prj.entity.Project;

@Repository("ProjectDaoImpl")
public class ProjectDaoImpl extends BaseDaoHibernateImpl<Project, Long>
        implements ProjectDao {

    public ProjectDaoImpl() {
        super(Project.class);
    }
    
    @Override
    public void update(Project project) {
        project.setModifyTime(DateUtils.getCurrentTimestamp());
        super.update(project);
    }

}
