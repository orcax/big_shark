/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config.security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prj.common.exception.AppException;
import com.prj.common.util.DataWrapper;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AppContrllerAdvise.java, v 0.1 Jun 21, 2015 11:01:02 AM yiliang.gyl Exp $
 */
@ControllerAdvice
public class AppContrllerAdvise {

    @ExceptionHandler(AppException.class)
    public void handleForbiddenException(HttpServletResponse response, AppException e)
                                                                                      throws IOException {
        response.setStatus(e.getStatusCode());
        response.setContentType("application/json;charset=UTF-8");
        DataWrapper dWrapper = new DataWrapper();
        dWrapper.setCode(e.getCode());
        dWrapper.setMessage(e.getMessage());
        dWrapper.setMessageDetail(e.getMessageDetail());
        response.getWriter().write(dWrapper.toString());
    }
}
