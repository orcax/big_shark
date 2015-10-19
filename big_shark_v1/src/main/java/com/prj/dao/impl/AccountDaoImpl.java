/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.AccountDao;
import com.prj.entity.Account;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AccountDaoImpl.java, v 0.1 Jun 21, 2015 2:28:59 PM yiliang.gyl Exp $
 */
@Repository("AccountDaoImpl")
public class AccountDaoImpl extends BaseDaoHibernateImpl<Account, Long> implements AccountDao {

    public AccountDaoImpl() {
        super(Account.class);
    }

}
