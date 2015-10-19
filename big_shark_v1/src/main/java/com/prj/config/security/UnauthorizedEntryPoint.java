/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: UnauthorizedEntryPoint.java, v 0.1 Jun 21, 2015 11:01:16 AM yiliang.gyl Exp $
 */
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException,
                                                               ServletException {
        // unauthenticated request not in permit list finally goes here
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
            new DataWrapper(ErrorCodeEnum.REQUEST_RESTRICTED,
                "Provide token or Post username & password.").toString());
    }
}
