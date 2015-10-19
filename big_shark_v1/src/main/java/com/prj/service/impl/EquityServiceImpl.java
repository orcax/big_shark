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
import com.prj.dao.EquityDao;
import com.prj.dao.EquityFileDao;
import com.prj.entity.Equity;
import com.prj.entity.EquityFile;
import com.prj.entity.EquityFile.Kind;
import com.prj.entity.EquityFile.Type;
import com.prj.service.EquityService;
import com.prj.service.FileService;

// TODO: Auto-generated Javadoc
/**
 * The Class EquityServiceImpl.
 */
@Service
public class EquityServiceImpl extends BaseServiceImpl<Equity, Long> implements
        EquityService {

    /** The equity dao. */
    EquityDao equityDao;

    /** The equity file dao. */
    @Autowired
    EquityFileDao equityFileDao;

    /** The file service. */
    @Autowired
    FileService fileService;

    /**
     * Instantiates a new equity service impl.
     *
     * @param equityDao the equity dao
     */
    @Autowired
    public EquityServiceImpl(EquityDao equityDao) {
        super(Equity.class, equityDao);
        this.equityDao = equityDao;
    }

    /** 
     * @see com.prj.service.EquityService#listByProject(java.lang.Long)
     */
    public DataWrapper listByProject(Long projectId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("projectId", projectId));
        return new DataWrapper(equityDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.BaseService#create(java.lang.Object)
     */
    public DataWrapper create(Equity equity) {
        return new DataWrapper(equityDao.create(equity));
    }

    /** 
     * @see com.prj.service.impl.BaseServiceImpl#update(java.io.Serializable, java.lang.Object)
     */
    @Override
    public DataWrapper update(Long equityId, Equity equity) {
        Equity data = equityDao.read(equityId);
        data.setBoard(equity.getBoard());
        data.setEstimateAmount(equity.getEstimateAmount());
        data.setInvestAmount(equity.getInvestAmount());
        data.setInvestCurrency(equity.getInvestCurrency());
        data.setInvestor(equity.getInvestor());
        data.setInvestTime(equity.getInvestTime());
        data.setInvestType(equity.getInvestType());
        data.setModifyTime(DateUtils.getCurrentTimestamp());
        data.setSharePercentage(equity.getSharePercentage());
        equityDao.update(data);
        return DataWrapper.voidSuccessRet;
    }

    @Override
    public DataWrapper delete(Long equityId) {
        Equity equity = equityDao.read(equityId);
        if (equity == null) {
            return DataWrapper.voidSuccessRet;
        }
        // 1) Delete equity files
        List<Criterion> c = new ArrayList<Criterion>();
        c.add(Restrictions.eq("equityId", equityId));
        List<EquityFile> efs = equityFileDao.getAllByConditions(c);
        for(EquityFile ef : efs) {
            deleteFile(ef.getId());
        }

        // 2) Delete equity
        equityDao.delete(equity);

        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.EquityService#getFile(java.lang.Long)
     */
    public EquityFile getFile(Long fileId) {
        return equityFileDao.read(fileId);
    }

    /** 
     * @see com.prj.service.EquityService#listFiles(java.lang.Long)
     */
    public DataWrapper listFiles(Long equityId) {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("equityId", equityId));
        return new DataWrapper(equityFileDao.getAllByConditions(conditions));
    }

    /** 
     * @see com.prj.service.EquityService#addFile(java.lang.Long, com.prj.entity.EquityFile.Type, org.springframework.web.multipart.MultipartFile)
     */
    public DataWrapper addFile(Long equityId, Type type, MultipartFile file) {
        String fileName = null;
        try {
            fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataWrapper(ErrorCodeEnum.BIZ_EXCEPTION,
                    "Equity file name cannot be decoded.");
        }
        long size = file.getSize();
        if (size > SystemConstants.MAX_ATTACH_SIZE) {
            return new DataWrapper(ErrorCodeEnum.BIZ_FILE_SIZE_EXCEED,
                    "Equity file size should be less than 5M.");
        }
        String suffix = fileName.substring(fileName.length() - 3).toUpperCase();
        Kind kind = null;
        try {
            kind = Kind.valueOf(suffix);
        } catch (Exception e) {
            return new DataWrapper(ErrorCodeEnum.BIZ_UNSUPPORTED_KIND,
                    "Unsupported file format");
        }
        String name = fileName.substring(0, fileName.length() - 4);

        // 1) Check if the type of file already exists.
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("equityId", equityId));
        conditions.add(Restrictions.eq("type", type));
        List<EquityFile> efs = equityFileDao.getAllByConditions(conditions);
        for (EquityFile ef : efs) {
            String fn = ef.getId() + "." + ef.getKind();
            fileService.delete(SystemConstants.EQUITY_FILE_PATH, fn);
            equityFileDao.delete(ef);
        }

        // 2) Save to database
        EquityFile ef = new EquityFile();
        ef.setEquityId(equityId);
        ef.setKind(kind);
        ef.setName(name);
        ef.setSize(size);
        ef.setType(type);
        Long efId = equityFileDao.create(ef);

        // 2) Save equity file on disk
        fileService.save(SystemConstants.EQUITY_FILE_PATH, efId + "." + kind,
                file);

        return new DataWrapper(efId);
    }

    /** 
     * @see com.prj.service.EquityService#readFile(java.lang.Long)
     */
    public FileObject readFile(Long equityFileId) {
        EquityFile ef = equityFileDao.read(equityFileId);
        if (ef == null) {
            return null;
        }
        String fileName = Long.toString(equityFileId) + "." + ef.getKind();
        byte[] data = fileService.read(SystemConstants.EQUITY_FILE_PATH,
                fileName);
        return new FileObject(ef, data);
    }

    /** 
     * @see com.prj.service.EquityService#deleteFile(java.lang.Long)
     */
    public DataWrapper deleteFile(Long equityFileId) {
        EquityFile ef = equityFileDao.read(equityFileId);
        if (ef != null) {
            String fileName = equityFileId + "." + ef.getKind();
            fileService.delete(SystemConstants.EQUITY_FILE_PATH, fileName);
            equityFileDao.delete(ef);
        }
        return DataWrapper.voidSuccessRet;
    }

}
