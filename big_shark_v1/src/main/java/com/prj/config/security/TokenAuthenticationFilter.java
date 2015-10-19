/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.MD5Tool;
import com.prj.entity.Account;
import com.prj.service.AccountService;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: TokenAuthenticationFilter.java, v 0.1 Jun 21, 2015 11:01:07 AM yiliang.gyl Exp $
 */
@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    @Autowired
    AuthenticationManager       authenticationManager;
    @Autowired
    AccountService              accountService;
    @Autowired
    TokenHandler                tokenHandler;
    private static final String HEADER_TOKEN    = "X-Auth-Token";
    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_PASSWORD = "X-Password";

    private static final long   EXPIRE_INTERVAL = 12 * 60 * 60 * 1000;

    private String              signOutUrl;
    private String              signInUrl;
    private List<String>        noLoginUrl;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                                                             throws IOException,
                                                                                             ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        long current = System.currentTimeMillis();
        String currentUrl = new UrlPathHelper().getPathWithinApplication(httpReq);
        String token = httpReq.getHeader(HEADER_TOKEN);

        // check URL in spring-security.properties
        Assert.hasText(signInUrl, "sign in url must be set");
        Assert.hasText(signOutUrl, "sign out url must be set");
        String username = httpReq.getHeader(HEADER_USERNAME);
        String password = httpReq.getHeader(HEADER_PASSWORD);
        if (currentUrl.equals(signInUrl) && !httpReq.getMethod().equals("DELETE")) {
            if (username != null && password != null && httpReq.getMethod().equals("POST")) {
                // check username and password
                password = MD5Tool.getMd5(password);
                Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
                try {
                    auth = authenticationManager.authenticate(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    if (auth.getPrincipal() != null) {
                        AccountUserDetails details = (AccountUserDetails) auth.getPrincipal();
                        details.eraseCredentials();
                        details.setExpires(current + EXPIRE_INTERVAL);

                        String fToken = tokenHandler.createTokenForAccount(details);

                        Account account = details.getAccount();
                        if (accountService.saveUserToken(account.getId(), fToken)) {
                            httpResp.setHeader(HEADER_TOKEN, fToken);
                            account.setToken(fToken);
                            setJsonResponse(httpResp, HttpServletResponse.SC_OK, new DataWrapper(
                                account));
                        } else {
                            setJsonResponse(httpResp, HttpServletResponse.SC_UNAUTHORIZED,
                                new DataWrapper(ErrorCodeEnum.SYSTEM_EXCEPTION, "系统错误"));
                        }
                    }
                } catch (AuthenticationException e) {
                    setJsonResponse(httpResp, HttpServletResponse.SC_UNAUTHORIZED, new DataWrapper(
                        ErrorCodeEnum.REQUEST_RESTRICTED, "账号或密码错误"));
                }
                // chain.doFilter(request, response);
            } else {
                setJsonResponse(httpResp, HttpServletResponse.SC_UNAUTHORIZED, new DataWrapper(
                    ErrorCodeEnum.REQUEST_RESTRICTED, "登录失败"));
            }
        } else if (token != null) {
            // check token
            AccountUserDetails userDetails = tokenHandler.parseAccountFromToken(token);
            if (userDetails != null) {
                Account account = (Account) accountService.read(userDetails.getAccount().getId())
                    .getData();
                if (account == null || !account.getToken().equals(token)
                    || current >= userDetails.getExpires()) {
                    setJsonResponse(httpResp, HttpServletResponse.SC_UNAUTHORIZED, new DataWrapper(
                        ErrorCodeEnum.REQUEST_RESTRICTED, "失效的登录"));
                } else {
                    // // extract logout URL in spring-security.properties file
                    if (currentUrl.equals(signOutUrl)) {
                        setJsonResponse(httpResp, HttpServletResponse.SC_OK, new DataWrapper(
                            ErrorCodeEnum.NO_ERROR, "已登出"));
                    } else {
                        UsernamePasswordAuthenticationToken securityToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(securityToken);
                        chain.doFilter(request, response);
                    }
                }
            } else {
                setJsonResponse(httpResp, HttpServletResponse.SC_UNAUTHORIZED, new DataWrapper(
                    ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE, "认证参数值错误"));
            }
        } else {
            if (noLoginUrl.contains(currentUrl)) {
                chain.doFilter(request, response);
            } else {
                setJsonResponse(httpResp, HttpServletResponse.SC_UNAUTHORIZED, new DataWrapper(
                    ErrorCodeEnum.PARAMETERS_IS_NULL, "参数缺失"));
            }
        }
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    public void setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
    }

    private void setJsonResponse(HttpServletResponse response, int stauts, DataWrapper ret) {
        response.setStatus(stauts);
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().write(ret.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public List<String> getNoLoginUrl() {
        return noLoginUrl;
    }

    public void setNoLoginUrl(List<String> noLoginUrl) {
        this.noLoginUrl = noLoginUrl;
    }

}
