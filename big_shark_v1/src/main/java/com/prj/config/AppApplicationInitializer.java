/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AppApplicationInitializer.java, v 0.1 Jun 21, 2015 11:00:24 AM yiliang.gyl Exp $
 */
@Order(2)
public class AppApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
                                                                                                   implements
                                                                                                   WebApplicationInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class, SecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { DispatcherConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
