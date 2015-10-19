/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prj.config.security.AccountUserDetailsService;
import com.prj.config.security.TokenAuthenticationFilter;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: SecurityConfig.java, v 0.1 Jun 21, 2015 11:00:42 AM yiliang.gyl Exp $
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
@PropertySource(value = { "/WEB-INF/spring-security.properties" })
@ComponentScan("com.prj")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    Environment               env;
    @Autowired
    AccountUserDetailsService asds;
    @Autowired
    TokenAuthenticationFilter tokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        tokenFilter.setSignInUrl(env.getProperty("url.signin"));
        tokenFilter.setSignOutUrl(env.getProperty("url.signout"));
        tokenFilter.setNoLoginUrl(Arrays.asList(env.getProperty("url.noLogin").split(",")));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .antMatcher("/api/**")
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class).csrf()
            .disable().authorizeRequests().antMatchers(env.getProperty("url.signin")).permitAll()
            .antMatchers("/api/**").hasAnyRole("ADMINISTRATOR", "CUSTOMER", "MANAGER");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(asds);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
