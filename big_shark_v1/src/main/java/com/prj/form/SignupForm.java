/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.form;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.prj.entity.Account;
import com.prj.entity.Account.Gender;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: SignupForm.java, v 0.1 Aug 9, 2015 2:13:58 AM yiliang.gyl Exp $
 */
public class SignupForm {
    private String account;
    private String password;
    private String name;
    private Gender gender;
    private String iconPath;

    public Account toAccount() {
        Account account = new Account();
        account.setAccount(this.account);
        account.setPassword(this.password);
        account.setGender(this.gender);
        account.setName(this.name);
        account.setIconPath(this.iconPath);
        return account;
    }

    /**
     * Getter method for property <tt>account</tt>.
     * 
     * @return property value of account
     */
    public String getAccount() {
        return account;
    }

    /**
     * Setter method for property <tt>account</tt>.
     * 
     * @param account value to be assigned to property account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Getter method for property <tt>password</tt>.
     * 
     * @return property value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for property <tt>password</tt>.
     * 
     * @param password value to be assigned to property password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for property <tt>gender</tt>.
     * 
     * @return property value of gender
     */
    @Enumerated(value = EnumType.STRING)
    public Gender getGender() {
        return gender;
    }

    /**
     * Setter method for property <tt>gender</tt>.
     * 
     * @param gender value to be assigned to property gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     * 
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * 
     * @return
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * Setter method for property <tt>iconPath</tt>.
     * 
     * @param name value to be assigned to property name
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

}
