/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: CopyRequired.java, v 0.1 Jun 21, 2015 10:58:55 AM yiliang.gyl Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CopyRequired {
    boolean create() default true;

    boolean update() default true;

    boolean copyWhenNull() default false;
}
