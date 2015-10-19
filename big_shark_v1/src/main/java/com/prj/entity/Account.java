/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.common.util.CopyRequired;

/**
 * 
 * @author yiliang.gyl
 * @version $Id: Account.java, v 0.1 Jun 21, 2015 11:54:57 AM yiliang.gyl Exp $
 */
@Entity
@Table(name = "account")
public class Account extends BaseEntity {

    public enum Role {
        ADMINISTRATOR, CUSTOMER, MANAGER;
        /**
         * add default rolePrefix 'ROLE_' to fit comparison in hasRole()
         * {@link org.springframework.security.access.expression.SecurityExpressionRoot#hasRole(String) }
         *
         * @return
         */
        public String toRoleString() {
            return "ROLE_" + super.toString();
        }
    }

    public enum Gender {
        MALE, FEMALE
    }

    private Gender  gender;
    private String  account;
    private String  password;     // 用户的密码，不返回
    private Role    role;
    private boolean active = true;
    private String  iconPath;
    private String  name;
    private String  title;
    private String  email;
    private String  mobile;
    private String  token;

    @CopyRequired
    @Column(nullable = false, unique = true)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @CopyRequired(update = false)
    @JsonIgnore
    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @CopyRequired
    @Column(name = "icon_path")
    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @CopyRequired(update = false)
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @CopyRequired
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @CopyRequired
    @Column
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @CopyRequired
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @CopyRequired
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @CopyRequired
    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @CopyRequired
    @Column
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
