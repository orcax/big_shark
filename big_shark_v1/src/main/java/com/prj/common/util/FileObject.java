package com.prj.common.util;

import java.util.HashMap;
import java.util.Map;

import com.prj.entity.BaseEntity;

public class FileObject {

    private static Map<String, String> kindMap = new HashMap<String, String>();
    static {
        kindMap.put("DOC", "application/msword");
        kindMap.put("JPG", "image/jpeg");
        kindMap.put("PDF", "application/pdf");
    }
    
    public static String getContentType(com.prj.entity.ProjectFile.Kind kind) {
        return kindMap.get(kind.toString());
    }
    
    public static String getContentType(com.prj.entity.EquityFile.Kind kind) {
        return kindMap.get(kind.toString());
    }

    private BaseEntity info;
    private byte[] data;

    public FileObject(BaseEntity info, byte[] data) {
        this.info = info;
        this.data = data;
    }

    public BaseEntity getInfo() {
        return info;
    }

    public void setInfo(BaseEntity info) {
        this.info = info;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
