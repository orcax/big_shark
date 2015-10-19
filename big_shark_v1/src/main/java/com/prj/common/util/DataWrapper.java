/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.util;

import com.prj.common.enums.ErrorCodeEnum;

/**
 * 对外访问使用的包装对象
 * @author yiliang.gyl
 * @version $Id: DataWrapper.java, v 0.1 Jun 21, 2015 10:59:01 AM yiliang.gyl Exp $
 */
public class DataWrapper {

    private String                  code;                                                          //返回码
    private String                  message;                                                       //返回码说明
    private String                  messageDetail;                                                 //错误消息

    private Object                  data;
    private int                     numPerPage;
    private int                     currPageNum;
    private int                     totalItemNum;
    private int                     totalPageNum;

    final public static DataWrapper voidSuccessRet = new DataWrapper(ErrorCodeEnum.NO_ERROR, true);

    public DataWrapper() {
        this(ErrorCodeEnum.NO_ERROR, new Object());
    }

    public DataWrapper(ErrorCodeEnum errorCode, Object data) {
        this.setCode(errorCode);
        this.data = data;
    }

    public DataWrapper(ErrorCodeEnum code, String detailsMessage) {
        this.code = code.getCode();
        this.message = code.getDefaultMessage();
        this.messageDetail = detailsMessage;
    }

    public DataWrapper(Object data) {
        this(ErrorCodeEnum.NO_ERROR, data);
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public int getCurrPageNum() {
        return currPageNum;
    }

    public void setCurrPageNum(int curPageNum) {
        this.currPageNum = curPageNum;
    }

    public int getTotalItemNum() {
        return totalItemNum;
    }

    public void setTotalItemNum(int totalItemNum) {
        this.totalItemNum = totalItemNum;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public void setErrorMessage(String errorMessage) {
        this.message = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(ErrorCodeEnum code) {
        this.code = code.getCode();
        this.message = code.getDefaultMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

}
