/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.dao.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.prj.common.util.DataWrapper;
import com.prj.dao.BaseDao;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: BaseDaoHibernateImpl.java, v 0.1 Jun 21, 2015 2:29:03 PM yiliang.gyl Exp $
 */
@Repository("BaseDaoHibernateImpl")
public class BaseDaoHibernateImpl<T, PK extends Serializable> implements BaseDao<T, PK> {
    @Autowired
    private SessionFactory sessionFactory;

    private Class<T>       type;

    public BaseDaoHibernateImpl() {
    }

    public BaseDaoHibernateImpl(Class<T> type) {
        this.type = type;
    }

    /**
     * 使用session.save，返回id
     *
     * @param object
     *            a transient instance of a persistent class
     *
     * @return the generated identifier
     */
    @SuppressWarnings("unchecked")
    public PK create(T o) {
        return (PK) getSession().save(o);
    }

    /**
     * 使用session.get，返回持久化对象或null
     *
     * @param clazz
     *            a persistent class
     * @param id
     *            an identifier
     *
     * @return a persistent instance or null
     */
    @SuppressWarnings("unchecked")
    public T read(PK id) {
        return (T) getSession().get(type, id);
    }

    /**
     * 使用update，若已有相同id的持久化对象则会引发HibernateException
     *
     * @param object
     *            a detached instance containing updated state
     */
    public void update(T o) {
        getSession().update(o);
    }

    public void update(PK id, String propertyName, Object value) {
        // Support entity without @Table or Table.name is ""
        String tableName = getTableName();
        StringBuffer buffer = new StringBuffer();
        buffer.append("update ");
        buffer.append(tableName);
        buffer.append(" set ");
        buffer.append(propertyName);
        buffer.append("=? where id=?");

        getSession().createSQLQuery(buffer.toString()).setParameter(0, value).setParameter(1, id)
            .executeUpdate();

    }

    /**
     * Apply an "equal" constraint to the named property
     *
     * @param propertyName
     *            The name of the property
     * @param value
     *            The value to use in comparison
     *
     * @return the single result or <tt>null</tt>
     *
     */
    @SuppressWarnings("unchecked")
    public T findBy(String propertyName, Object value) {
        return (T) getSession().createCriteria(type).add(Restrictions.eq(propertyName, value))
            .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<T> listBy(String propertyName, Object value) {
        return getSession().createCriteria(type).add(Restrictions.eq(propertyName, value)).list();
    }

    /**
     * 使用session.delete
     *
     * @param object
     *            the instance to be removed
     */
    public void delete(T o) {
        getSession().delete(o);
    }

    public boolean isUnique(String propertyName, Object value) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select count(*) from ");
        buffer.append(type.getSimpleName());
        buffer.append(" where ");
        buffer.append(propertyName);
        buffer.append("=?");
        return ((Long) getSession().createQuery(buffer.toString()).setParameter(0, value)
            .uniqueResult()).intValue() == 0;
    }

    private static int getTotalPageNum(Integer totalItemNum, Integer pageSize) {
        if (totalItemNum % pageSize == 0)
            return totalItemNum / pageSize;
        else
            return totalItemNum / pageSize + 1;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private String getTableName() {
        Annotation[] as = type.getAnnotations();
        Table t = null;
        if (as.length == 0)
            return type.getName();
        for (Annotation a : as) {
            if (a instanceof Table) {
                t = (Table) a;
            }
        }
        if (t == null)
            return null;
        Method m;
        try {
            m = t.getClass().getMethod("name");
            String tmp = (String) m.invoke(t);
            if (tmp.equals(""))
                return type.getName();
            else
                return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 支持条件 get all 支持
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllByConditions(List<Criterion> conditions) {
        Criteria crt = getSession().createCriteria(this.type);
        for (Criterion exp : conditions) {
            crt.add(exp);
        }
        crt.addOrder(Order.desc("modifyTime"));
        crt.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return crt.list();
    }

    public int countByConditions(List<Criterion> conditions) {
        Criteria crt = getSession().createCriteria(this.type);
        for (Criterion exp : conditions) {
            crt.add(exp);
        }
        int totalCount = Integer.parseInt(crt.setProjection(Projections.rowCount()).uniqueResult()
            .toString());
        return totalCount;
    }

    /**
     * 按照条件分页列出对象
     */
    public DataWrapper getPageByConditions(int pageNum, int pageSize, List<Criterion> conditions) {
        DataWrapper dw = new DataWrapper();
        Criteria crt = getSession().createCriteria(this.type);
        for (Criterion exp : conditions) {
            crt.add(exp);
        }
        // 查询总数
        int totalCount = Integer.parseInt(crt.setProjection(Projections.rowCount()).uniqueResult()
            .toString());
        dw.setTotalItemNum(totalCount);

        crt.setProjection(null);
        crt.setFirstResult((pageNum - 1) * pageSize);
        crt.setMaxResults(pageSize);
        Disjunction re = Restrictions.or();
        crt.add(re);
        crt.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<T> list = crt.list();
        dw.setCurrPageNum(pageNum);
        dw.setNumPerPage(pageSize);
        dw.setTotalPageNum(getTotalPageNum(totalCount, pageSize));
        dw.setData(list);
        return dw;
    }

    /**
     * 按照条件分页列出对象
     */
    public DataWrapper getPageByConditions(int pageNum, int pageSize, List<Criterion> conditions,
                                           List<Order> orders) {
        DataWrapper dw = new DataWrapper();
        Criteria crt = getSession().createCriteria(this.type);
        for (Criterion exp : conditions) {
            crt.add(exp);
        }
        for (Order order : orders) {
            crt.addOrder(order);
        }
        // 查询总数
        int totalCount = Integer.parseInt(crt.setProjection(Projections.rowCount()).uniqueResult()
            .toString());
        dw.setTotalItemNum(totalCount);

        crt.setProjection(null);
        crt.setFirstResult((pageNum - 1) * pageSize);
        crt.setMaxResults(pageSize);
        Disjunction re = Restrictions.or();
        crt.add(re);
        crt.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<T> list = crt.list();
        dw.setCurrPageNum(pageNum);
        dw.setNumPerPage(pageSize);
        dw.setTotalPageNum(getTotalPageNum(totalCount, pageSize));
        dw.setData(list);
        return dw;
    }

    public Criteria createCriteria() {
        return getSession().createCriteria(type);
    }

    public DataWrapper getPageByCriteria(int pageNum, int pageSize, Criteria crt) {
        DataWrapper dw = new DataWrapper();
        // 查询总数
        int totalCount = Integer.parseInt(crt.setProjection(Projections.rowCount()).uniqueResult()
            .toString());
        dw.setTotalItemNum(totalCount);

        crt.setProjection(null);
        crt.setFirstResult((pageNum - 1) * pageSize);
        crt.setMaxResults(pageSize);
        Disjunction re = Restrictions.or();
        crt.add(re);
        crt.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("rawtypes")
        List list = crt.list();
        dw.setCurrPageNum(pageNum);
        dw.setNumPerPage(pageSize);
        dw.setTotalPageNum(getTotalPageNum(totalCount, pageSize));
        dw.setData(list);
        return dw;
    }

    public DataWrapper getall() {
        return new DataWrapper(getSession().createCriteria(type).addOrder(Order.desc("modifyTime"))
            .list());
    }

    public DataWrapper getPage(int pageNum, int pagesize) {
        return getPageByConditions(pageNum, pagesize, new ArrayList<Criterion>());
    }

}
