/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AppConfig.java, v 0.1 Jun 21, 2015 11:00:28 AM yiliang.gyl Exp $
 */
@Configuration
@EnableTransactionManagement
@PropertySources(value = { @PropertySource("/WEB-INF/persistence-mysql.properties") })
@EnableCaching
@ComponentScan("com.prj")
public class AppConfig implements TransactionManagementConfigurer {
    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource(env.getProperty("jdbc.url"),
            env.getProperty("jdbc.user"), env.getProperty("jdbc.pass"));
        ds.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        return ds;
    }

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration config = new LocalSessionFactoryBuilder(dataSource())
            .scanPackages("com.prj.entity")
            .setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"))
            .setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"))
            .setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
            config.getProperties()).build();
        return config.buildSessionFactory(serviceRegistry);
    }

    /**
     * 配置缓存
     * @return
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(ehCacheFactoryBean().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheFactoryBean() {
        EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
        cacheManager.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManager.setShared(true);
        return cacheManager;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }

}
