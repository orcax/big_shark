/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prj.entity.Account;
import com.prj.service.AccountService;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AccountUserDetailsService.java, v 0.1 Jun 21, 2015 11:00:57 AM yiliang.gyl Exp $
 */
@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    AccountService as;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account a = (Account) as.findByAccount(username).getData();
        if (a == null)
            throw new UsernameNotFoundException("The user could not be found.");
        return new AccountUserDetails(a);
    }

    public boolean persistUserToken(Long id, String token) {
        return as.saveUserToken(id, token);
    }

}
