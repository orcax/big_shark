package com.prj.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.DateUtils;
import com.prj.common.util.FileObject;
import com.prj.common.util.SystemConstants;
import com.prj.dao.ProjectDao;
import com.prj.dao.ProjectFileDao;
import com.prj.entity.Project;
import com.prj.entity.Project.InvestType;
import com.prj.entity.Project.Phase;
import com.prj.entity.Project.Status;
import com.prj.entity.ProjectFile;
import com.prj.entity.ProjectFile.Kind;
import com.prj.service.CompanyService;
import com.prj.service.FileService;
import com.prj.service.ProjectService;

/**
 * The Class ProjectServiceImpl.
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project, Long> implements ProjectService {

    /** The project dao. */
    ProjectDao     projectDao;

    /** The project file dao. */
    @Autowired
    ProjectFileDao projectFileDao;

    /** The file service. */
    @Autowired
    FileService    fileService;

    /** The company service. */
    @Autowired
    CompanyService companyService;

    /**
     * Instantiates a new project service impl.
     *
     * @param projectDao the project dao
     */
    @Autowired
    ProjectServiceImpl(ProjectDao projectDao) {
        super(Project.class, projectDao);
        this.projectDao = projectDao;
    }

    /** 
     * @see com.prj.service.ProjectService#isValid(java.lang.Long, java.lang.Long, com.prj.entity.Project.Phase)
     */
    public boolean isValid(Long accountId, Long projectId, Phase phase) {
        Project p = projectDao.read(projectId);
        return p != null && p.getCreaterId() == accountId && p.getPhase() == phase;
    }

    /** 
     * @see com.prj.service.ProjectService#isValid(java.lang.Long, java.lang.Long)
     */
    public boolean isValid(Long accountId, Long projectId) {
        Project p = projectDao.read(projectId);
        return p != null && p.getCreaterId() == accountId;
    }

    /** 
     * @see com.prj.service.ProjectService#isValid(java.lang.Long, com.prj.entity.Project.Phase)
     */
    public boolean isValid(Long projectId, Phase phase) {
        Project p = projectDao.read(projectId);
        return p != null && p.getPhase() == phase;
    }

    /** 
     * @see com.prj.service.impl.BaseServiceImpl#get(java.io.Serializable)
     */
    public Project get(Long projectId) {
        return projectDao.read(projectId);
    }

    /** 
     * @see com.prj.service.ProjectService#listByCreater(java.lang.Long)
     */
    public DataWrapper listByCreater(Long createrId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("createrId", createrId));
        return new DataWrapper(projectDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.ProjectService#search(com.prj.entity.Project.Phase, com.prj.entity.Project.Status, java.lang.String, java.lang.String, java.lang.Boolean, com.prj.entity.Project.InvestType, java.lang.Integer, java.lang.Integer)
     */
    public DataWrapper search(Phase phase, Status status, String city, String name,
                              Boolean notified, InvestType investType, Integer pageSize,
                              Integer pageNumber) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        if (phase == null) {
            conditions.add(Restrictions.ne("phase", Phase.DRAFT));
        } else {
            conditions.add(Restrictions.eq("phase", phase));
        }
        if (status == null) {
            status = Status.NORMAL;
        }
        conditions.add(Restrictions.eq("status", status));
        if (city != null) {
            conditions.add(Restrictions.eq("city", city));
        }
        if (name != null) {
            conditions.add(Restrictions.ilike("name", "%" + name + "%"));
        }
        if (notified != null) {
            conditions.add(Restrictions.eq("notified", notified));
        }
        if (investType != null) {
            conditions.add(Restrictions.eq("investType", investType));
        }
        if (pageSize == null || pageNumber == null) {
            return new DataWrapper(projectDao.getAllByConditions(conditions));
        }
        return new DataWrapper(projectDao.getPageByConditions(pageNumber, pageSize, conditions));
    }

    /** 
     * @see com.prj.service.BaseService#create(java.lang.Object)
     */
    public DataWrapper create(Project project) {
        project.setPhase(Phase.DRAFT);
        project.setStatus(Status.NORMAL);
        project.setNotified(false);
        project.setInvestType(null);
        project.setFileCount(0);
        return new DataWrapper(projectDao.create(project));
    }

    /** 
     * @see com.prj.service.impl.BaseServiceImpl#update(java.io.Serializable, java.lang.Object)
     */
    @Override
    public DataWrapper update(Long projectId, Project p) {
        Project project = projectDao.read(projectId);
        project.setContactName(p.getContactName());
        project.setContactEmail(p.getContactEmail());
        project.setContactMobile(p.getContactMobile());
        project.setName(p.getName());
        project.setStartDate(p.getStartDate());
        project.setCity(p.getCity());
        project.setType(p.getType());
        project.setDescription(p.getDescription());
        projectDao.update(project);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.ProjectService#submit(java.lang.Long)
     */
    public DataWrapper submit(Long projectId) {
        Project project = projectDao.read(projectId);
        project.setPhase(Phase.AUDITION);
        projectDao.update(project);
        // TODO +email notification
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.ProjectService#readFile(java.lang.Long)
     */
    public FileObject readFile(Long fileId) {
        ProjectFile pf = projectFileDao.read(fileId);
        if (pf == null) {
            return null;
        }
        String fileName = Long.toString(fileId) + "." + pf.getKind();
        byte[] data = fileService.read(SystemConstants.ATTACHMENT_PATH, fileName);
        return new FileObject(pf, data);
    }

    /** 
     * @see com.prj.service.ProjectService#listFiles(java.lang.Long)
     */
    public DataWrapper listFiles(Long projectId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", projectId));
        return new DataWrapper(projectFileDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.ProjectService#addFile(java.lang.Long, org.springframework.web.multipart.MultipartFile)
     */
    public DataWrapper addFile(Long projectId, MultipartFile file) {
        String fileName = null;
        try {
            fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataWrapper(ErrorCodeEnum.BIZ_EXCEPTION,
                "Project file name cannot be decoded.");
        }
        if (file.getSize() > SystemConstants.MAX_ATTACH_SIZE) {
            return new DataWrapper(ErrorCodeEnum.BIZ_FILE_SIZE_EXCEED,
                "Project file size should be less than 5M.");
        }
        Project project = projectDao.read(projectId);
        if (project.getFileCount() >= SystemConstants.MAX_ATTACH_COUNT) {
            return new DataWrapper(ErrorCodeEnum.BIZ_FILE_COUNT_EXCEED,
                "At most 5 files can be added to project.");
        }

        // 1) Save attachment info in database
        String kind = fileName.substring(fileName.length() - 3).toUpperCase();
        String name = fileName.substring(0, fileName.length() - 4);
        ProjectFile pf = new ProjectFile();
        try {
            pf.setKind(Kind.valueOf(kind));
        } catch (Exception e) {
            return new DataWrapper(ErrorCodeEnum.BIZ_UNSUPPORTED_KIND, "Unsupported file format");
        }
        pf.setProjectId(project.getId());
        pf.setName(name);
        pf.setSize(file.getSize());
        Long pfId = projectFileDao.create(pf);

        // 2) Save attachment file on disk
        fileService.save(SystemConstants.ATTACHMENT_PATH, pfId + "." + kind, file);

        // 3) Increment attachment count in project table
        project.incrementFileCount();
        projectDao.update(project);

        return new DataWrapper(pfId);
    }

    /** 
     * @see com.prj.service.ProjectService#deleteFile(java.lang.Long)
     */
    public DataWrapper deleteFile(Long fileId) {
        ProjectFile pf = projectFileDao.read(fileId);
        if (pf == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                "Project file record is not found.");
        }

        // 1) Decrement attachment count in project table
        Project project = projectDao.read(pf.getProjectId());
        if (project.getFileCount() <= 0) {
            return new DataWrapper(ErrorCodeEnum.BIZ_FILE_COUNT_EXCEED,
                "At most 5 files can be added to project.");
        }
        project.decrementFileCount();
        projectDao.update(project);

        // 2) Delete file on disk
        String fileName = fileId + "." + pf.getKind();
        fileService.delete(SystemConstants.ATTACHMENT_PATH, fileName);

        // 3) Delete record in database
        projectFileDao.delete(pf);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.ProjectService#changePhase(java.lang.Long, com.prj.entity.Project.Phase, com.prj.entity.Project.InvestType)
     */
    public DataWrapper changePhase(Long projectId, Phase nextPhase, InvestType investType) {
        if (nextPhase == Phase.DRAFT) {
            return new DataWrapper(ErrorCodeEnum.BIZ_PHASE_ERROR, "Cannot change phase to DRAFT.");
        }
        Project project = projectDao.read(projectId);
        if (project == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND, "The project does not exist.");
        }
        if (project.getPhase() == Phase.DRAFT || project.getPhase() == Phase.ACCEPTED) {
            return new DataWrapper(ErrorCodeEnum.BIZ_PHASE_ERROR, String.format(
                "Cannot change phase if the project is in %s.", project.getPhase()));
        }
        if (nextPhase == Phase.FINAL_REVIEW) {
            if (investType == null) {
                return new DataWrapper(ErrorCodeEnum.BIZ_DATA_INSUFFICIENT, "Invest Type is empty.");
            }
            project.setInvestType(investType);
        }
        /*
        if (nextPhase == Phase.ACCEPTED) {
            if (companyService.readByProject(projectId) == null) {
                Company company = new Company();
                company.setProjectId(projectId);
                companyService.create(company);
            }
        }
        */
        project.setPhase(nextPhase);
        project.setNotified(false);
        project.setModifyTime(DateUtils.getCurrentTimestamp());
        projectDao.update(project);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.ProjectService#batchNotify(long[])
     */
    public DataWrapper batchNotify(long[] projectIds) {
        for (long pid : projectIds) {
            projectDao.update(pid, "notified", true);
        }
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.ProjectService#changeStatus(java.lang.Long, com.prj.entity.Project.Status)
     */
    public DataWrapper changeStatus(Long projectId, Status status) {
        Project project = projectDao.read(projectId);
        if (project.getPhase() == Phase.DRAFT || project.getPhase() == Phase.ACCEPTED) {
            return new DataWrapper(ErrorCodeEnum.BIZ_PHASE_ERROR, String.format(
                "Cannot change status at phase %s.", project.getPhase()));
        }
        project.setStatus(status);
        project.setModifyTime(DateUtils.getCurrentTimestamp());
        projectDao.update(project);
        return DataWrapper.voidSuccessRet;
    }

}
