package com.prj.service;

import org.springframework.web.multipart.MultipartFile;

import com.prj.common.util.DataWrapper;
import com.prj.common.util.FileObject;
import com.prj.entity.Equity;
import com.prj.entity.EquityFile;
import com.prj.entity.EquityFile.Type;

public interface EquityService extends BaseService<Equity, Long> {
    
    DataWrapper listByProject(Long projectId);
    
    DataWrapper listFiles(Long equityId);
    
    EquityFile getFile(Long fileId);
    
    DataWrapper addFile(Long equityId, Type type, MultipartFile file);
    
    FileObject readFile(Long equityFileId);
    
    DataWrapper deleteFile(Long equityFileId);

}
