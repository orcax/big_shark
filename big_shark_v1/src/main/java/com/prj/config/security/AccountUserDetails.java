/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.config.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prj.entity.Account;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: AccountUserDetails.java, v 0.1 Jun 21, 2015 11:00:54 AM yiliang.gyl Exp $
 */
@JsonIgnoreProperties(value = { "authorities" }, ignoreUnknown = true)
public class AccountUserDetails implements UserDetails, CredentialsContainer {

    private static final long                 serialVersionUID = 2745987116881538294L;

    private Account                           account;
    private long                              expires;

    private ArrayList<SimpleGrantedAuthority> grantedAuths;

    public AccountUserDetails() {
    }

    public AccountUserDetails(Account account) {
        this.account = account;
        this.grantedAuths = new ArrayList<SimpleGrantedAuthority>(1);
        this.grantedAuths.add(new SimpleGrantedAuthority(account.getRole().toRoleString()));
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuths;
    }

    public ArrayList<String> getGrantedAuths() {
        ArrayList<String> l = new ArrayList<String>(1);
        for (SimpleGrantedAuthority auth : grantedAuths) {
            l.add(auth.getAuthority());
        }
        return l;
    }

    public void setGrantedAuths(ArrayList<String> authStrs) {
        this.grantedAuths = new ArrayList<SimpleGrantedAuthority>(1);
        for (String authStr : authStrs) {
            this.grantedAuths.add(new SimpleGrantedAuthority(authStr));
        }
    }

    public String getPassword() {
        return this.account.getPassword();
    }

    public String getUsername() {
        return this.account.getAccount();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public void eraseCredentials() {
        this.account.setPassword(null);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getExpires() {
        return this.expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

}
