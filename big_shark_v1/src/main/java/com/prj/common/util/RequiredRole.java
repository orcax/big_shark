/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.prj.entity.Account.Role;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: RequiredRole.java, v 0.1 Jun 21, 2015 10:59:39 AM yiliang.gyl Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiredRole {
    Role[] value();

    // boolean privateAccess() default false;
}
