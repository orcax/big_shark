/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.exception;

import javax.servlet.http.HttpServletResponse;

import com.prj.common.enums.ErrorCodeEnum;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AppException.java, v 0.1 Jun 21, 2015 10:57:19 AM yiliang.gyl
 *          Exp $
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 4925495525292795163L;

    private int               statusCode;
    private String            code;
    private String            message;
    private String            messageDetail;

    public AppException(int statusCode, ErrorCodeEnum ErrorCodeEnum, String message) {
        super(message);
        this.statusCode = statusCode;
        this.code = ErrorCodeEnum.getCode();
        this.message = message;
    }

    public static AppException newInstanceOfNotFoundException(String entity) {
        return new AppException(HttpServletResponse.SC_NOT_FOUND, ErrorCodeEnum.NOT_FOUND, entity);
    }

    public static AppException newInstanceOfDuplicationException(String property) {
        return new AppException(HttpServletResponse.SC_CONFLICT, ErrorCodeEnum.BIZ_DUPLICATIVE,
            property);
    }

    public static AppException newInstanceOfWrongParameterException() {
        return new AppException(HttpServletResponse.SC_BAD_REQUEST,
            ErrorCodeEnum.ILLEGAL_PARAMETER_NAME, "Parameters don't meet restrictions.");
    }

    public static AppException newInstanceOfForbiddenException(String accountName) {
        return new AppException(HttpServletResponse.SC_FORBIDDEN, ErrorCodeEnum.BIZ_EXCEPTION,
            accountName + " is not Onwer");
    }

    public static AppException newInstanceOfPasswordException() {
        return new AppException(418, ErrorCodeEnum.BIZ_FAIL, "密码错误");
    }

    public static AppException newInstanceOfInternalException(String msg) {
        return new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            ErrorCodeEnum.SYSTEM_EXCEPTION, msg);
    }

    /**
     * Getter method for property <tt>statusCode</tt>.
     * 
     * @return property value of statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Setter method for property <tt>statusCode</tt>.
     * 
     * @param statusCode
     *            value to be assigned to property statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     * 
     * @param code
     *            value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter method for property <tt>message</tt>.
     * 
     * @return property value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for property <tt>message</tt>.
     * 
     * @param message
     *            value to be assigned to property message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter method for property <tt>messageDetail</tt>.
     * 
     * @return property value of messageDetail
     */
    public String getMessageDetail() {
        return messageDetail;
    }

    /**
     * Setter method for property <tt>messageDetail</tt>.
     * 
     * @param messageDetail
     *            value to be assigned to property messageDetail
     */
    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

}
