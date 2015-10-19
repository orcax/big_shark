/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.prj.common.util.DataWrapper;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: BaseDao.java, v 0.1 Jun 21, 2015 2:28:54 PM yiliang.gyl Exp $
 */
public interface BaseDao<T, PK extends Serializable> {

    PK create(T o);

    T read(PK id);

    void update(T o);

    void delete(T o);

    T findBy(String propertyName, Object value);

    void update(PK id, String propertyName, Object value);

    boolean isUnique(String propertyName, Object value);

    int countByConditions(List<Criterion> conditions);

    List<T> getAllByConditions(List<Criterion> conditions);

    DataWrapper getPageByConditions(int pageNum, int pageSize, List<Criterion> conditions);

    DataWrapper getPageByConditions(int pageNum, int pageSize, List<Criterion> conditions,
                                    List<Order> orders);

    Criteria createCriteria();

    DataWrapper getPageByCriteria(int pageNum, int pageSize, Criteria criteria);

    DataWrapper getall();

}
