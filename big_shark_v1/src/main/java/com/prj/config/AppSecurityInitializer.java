/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AppSecurityInitializer.java, v 0.1 Jun 21, 2015 11:00:31 AM yiliang.gyl Exp $
 */
@Order(1)
public class AppSecurityInitializer extends AbstractSecurityWebApplicationInitializer implements
                                                                                     WebApplicationInitializer {

}
