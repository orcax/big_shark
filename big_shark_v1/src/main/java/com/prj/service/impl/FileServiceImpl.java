package com.prj.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prj.common.exception.AppException;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.RootPath;
import com.prj.service.FileService;

/**
 * The Class FileServiceImpl.
 */
@Service
public class FileServiceImpl implements FileService {

    /** 
     * @see com.prj.service.FileService#save(java.lang.String, java.lang.String, org.springframework.web.multipart.MultipartFile)
     */
    public DataWrapper save(String relativePath, String fileName, MultipartFile file) {
        if (file == null) {
            throw AppException.newInstanceOfInternalException("Null File.");
        }
        try {
            String path = RootPath.value + relativePath;
            if (!new File(path).isDirectory()) {
                new File(path).mkdirs();
            }
            if (fileName == null) {
                fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
            }
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
            System.out.println("Creating File: " + path + "/" + fileName);
            InputStream is = file.getInputStream();
            byte[] buffer = new byte[1024 * 1024];
            int byteread = 0;
            while ((byteread = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteread);
                fos.flush();
            }
            fos.close();
            is.close();
            return new DataWrapper(relativePath + "/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw AppException.newInstanceOfInternalException("File creation error.");
        }
    }

    /** 
     * @see com.prj.service.FileService#read(java.lang.String, java.lang.String)
     */
    public byte[] read(String relativePath, String fileName) {
        String path = RootPath.value + relativePath + "/" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            throw AppException.newInstanceOfInternalException("File does not exist.");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream in = null;
        byte[] result = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while ((len = in.read(buffer, 0, bufSize)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw AppException.newInstanceOfInternalException("File read error.");
        } finally {
            try {
                in.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /** 
     * @see com.prj.service.FileService#delete(java.lang.String, java.lang.String)
     */
    public void delete(String relativePath, String fileName) {
        String path = RootPath.value + relativePath + "/" + fileName;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
}
