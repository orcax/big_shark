/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.controller;

import java.net.URLDecoder;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.RequiredRole;
import com.prj.common.util.SystemConstants;
import com.prj.entity.Account;
import com.prj.entity.Account.Role;
import com.prj.form.SignupForm;
import com.prj.query.CustomerQuery;
import com.prj.query.ManagerQuery;

/**
 * @author HuPeng
 * @version $Id: AccountController.java, v 0.1 2015年8月7日 下午3:28:51 HuPeng Exp $
 */
@RestController
public class AccountController extends BaseController {

    /**
     * 8.更新用户信息
     * PUT /account/profile
     * <p>
     * BODY Account
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/me/profile", method = RequestMethod.PUT)
    @ResponseBody
    @RequiredRole({ Role.CUSTOMER, Role.MANAGER })
    DataWrapper updateProfile(@RequestBody Account account) {
        return as.update(currentAccount().getId(), account);
    }

    /**
     * 8.更新用户信息
     * PUT /account/profile
     * <p>
     * BODY Account
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/{id}/profile", method = RequestMethod.PUT)
    @ResponseBody
    @RequiredRole({ Role.ADMINISTRATOR })
    DataWrapper updateProfileById(@RequestBody Account account, @PathVariable long id) {
        return as.update(id, account);
    }

    /**
     * GET /account/profile
     * 4.获取当前用户信息
     * @return
     */
    @RequestMapping(value = "/account/me/profile", method = RequestMethod.GET)
    @ResponseBody
    @RequiredRole(Role.CUSTOMER)
    DataWrapper getProfile() {
        return as.read(currentAccount().getId());
    }

    /**
     * GET /account/profile
     * 4.获取当前用户信息
     * @return
     */
    @RequestMapping(value = "/account/{id}/profile", method = RequestMethod.GET)
    @ResponseBody
    @RequiredRole({ Role.ADMINISTRATOR, Role.MANAGER })
    DataWrapper getProfileById(@PathVariable long id) {
        return as.read(id);
    }

    /**
     * PUT /account/password
     * <p>
     * BODY {oldPassword, newPassword}
     * <p>
     * Reset password by oneself or administrator
     *  9.修改密码
     * @param id
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/me/password", method = RequestMethod.PUT)
    @ResponseBody
    @RequiredRole(Role.CUSTOMER)
    DataWrapper updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return as.updatePassword(currentAccount().getId(), oldPassword, newPassword);
    }

    /**
     * 3.普通用户注册
     * 
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/customer", method = RequestMethod.POST)
    @ResponseBody
    DataWrapper register(@RequestBody SignupForm account) {
        Account a = account.toAccount();
        a.setRole(Role.CUSTOMER);
        return as.create(a);
    }

    /**
     * 发送验证码
     * 
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/account/captcha", method = RequestMethod.POST)
    @ResponseBody
    @RequiredRole(Role.CUSTOMER)
    DataWrapper sendCode(@RequestParam("leaderMobile") String mobile) {
        return as.sendCode(mobile);
    }

    /**
     * 验证验证码的正确性
     * 
     * @param mobile
     * @param code
     * @return
     */
    @RequestMapping(value = "/account/captcha/verify", method = RequestMethod.PUT)
    @ResponseBody
    @RequiredRole(Role.CUSTOMER)
    DataWrapper verifyCode(@RequestParam("leaderMobile") String mobile, @RequestParam String code) {
        return as.verifyCode(mobile, code);
    }

    /**
     * 校验邮箱
     * 
     * @param email
     * @return
     */
    @RequestMapping(value = "/account/email/verify", method = RequestMethod.PUT)
    @ResponseBody
    @RequiredRole(Role.CUSTOMER)
    DataWrapper verifyEmail(@RequestParam String email) {
        return as.verifyEmail(email);
    }

    @RequestMapping(value = "/account/icon", method = RequestMethod.POST)
    @ResponseBody
    DataWrapper uploadIcon(@RequestParam MultipartFile icon) {
        String fileName = null;
        try {
            fileName = URLDecoder.decode(icon.getOriginalFilename(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataWrapper(ErrorCodeEnum.BIZ_EXCEPTION, "File name cannot be decoded.");
        }
        return fileService.save(SystemConstants.UPLOAD_PATH, fileName, icon);
    }

    /**
     * 管理员列表
     * 
     * @param query
     * @return
     */
    @RequestMapping(value = "/account/manager/list", method = RequestMethod.GET)
    @ResponseBody
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper managerQuery(ManagerQuery query) {
        return as.pageQuery(query);
    }

    /**
     * 普通用户列表
     * 
     * @param query
     * @return
     */
    @RequestMapping(value = "/account/customer/list", method = RequestMethod.GET)
    @ResponseBody
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper customerQuery(CustomerQuery query) {
        return as.pageQuery(query);
    }

    /**
     * 3.普通用户注册
     * 
     * @param data
     * @return
     */
    @RequestMapping(value = "/account/manager", method = RequestMethod.POST)
    @ResponseBody
    @RequiredRole(Role.ADMINISTRATOR)
    DataWrapper createManager(@RequestBody SignupForm account) {
        Account a = account.toAccount();
        a.setRole(Role.MANAGER);
        return as.create(a);
    }
}
