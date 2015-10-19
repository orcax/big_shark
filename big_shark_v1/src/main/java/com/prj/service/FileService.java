package com.prj.service;

import org.springframework.web.multipart.MultipartFile;

import com.prj.common.util.DataWrapper;

public interface FileService {

    public DataWrapper save(String relativePath, String fileName, MultipartFile file);

    public byte[] read(String relativePath, String fileName);

    public void delete(String relativePath, String fileName);
}
