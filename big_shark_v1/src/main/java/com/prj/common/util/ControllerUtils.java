/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prj.config.security.AccountUserDetails;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: ControllerUtils.java, v 0.1 Jun 21, 2015 11:00:14 AM yiliang.gyl Exp $
 */
public class ControllerUtils {

    /**
     * 在 Controller 中获取对象
     * @return
     */
    public static AccountUserDetails currentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return (AccountUserDetails) auth.getPrincipal();
        }
        return null;
    }
}
