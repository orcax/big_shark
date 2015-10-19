/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.CaptchaUtils;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.MD5Tool;
import com.prj.common.util.RootPath;
import com.prj.common.util.SystemConstants;
import com.prj.dao.AccountDao;
import com.prj.dao.CaptchaDao;
import com.prj.dao.ProjectDao;
import com.prj.entity.Account;
import com.prj.entity.Captcha;
import com.prj.service.AccountService;

/**
 * The Class AccountServiceImpl.
 *
 * @author Hupeng
 * @version $Id: AccountServiceImpl.java, v 0.1 2015年8月7日 下午3:19:04 Hupeng Exp $
 */
@PropertySource(value = { "/WEB-INF/spring-security.properties" })
@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, Long> implements AccountService {

    /** The logger. */
    public static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    /** The env. */
    @Autowired
    Environment          env;

    /** The project dao. */
    @Autowired
    ProjectDao           projectDao;

    /** The captcha dao. */
    @Autowired
    CaptchaDao           captchaDao;

    /** The account dao. */
    AccountDao           accountDao;

    /**
     * Instantiates a new account service impl.
     *
     * @param ad the ad
     */
    @Autowired
    public AccountServiceImpl(AccountDao ad) {
        super(Account.class, ad);
        this.accountDao = ad;
    }

    /** 
     * @see com.prj.service.AccountService#readObject(long)
     */
    @Cacheable(value = "userCache", key = "'user1' + #id")
    public DataWrapper readObject(long id) {
        return new DataWrapper(accountDao.read(id));
    }

    /**
     * Creates the.
     *
     * @param a the a
     * @return the data wrapper
     * @see com.prj.service.BaseService#create(java.lang.Object)
     */
    public DataWrapper create(Account a) {
        assertUniqueAccount(a.getAccount());
        a.setPassword(MD5Tool.getMd5(a.getPassword()));
        a.setEmail(a.getAccount());
        return super.createEntity(a);
    }

    /** 
     * @see com.prj.service.AccountService#findByAccount(java.lang.String)
     */
    public DataWrapper findByAccount(String account) {
        return findBy("account", account);
    }

    /** 
     * @see com.prj.service.AccountService#updatePassword(long, java.lang.String, java.lang.String)
     */
    public DataWrapper updatePassword(long id, String oldPassword, String newPassword) {
        Account a = (Account) read(id).getData();
        if (MD5Tool.getMd5(oldPassword).equals(a.getPassword())) {
            a.setPassword(MD5Tool.getMd5(newPassword));
            accountDao.update(a);
            return DataWrapper.voidSuccessRet;
        } else {
            return new DataWrapper(false);
        }
    }

    /** 
     * @see com.prj.service.AccountService#updatePasswordByAdmin(long, java.lang.String)
     */
    public DataWrapper updatePasswordByAdmin(long id, String newPassword) {
        Account a = (Account) read(id).getData();
        a.setPassword(MD5Tool.getMd5(newPassword));
        accountDao.update(a);
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.AccountService#saveUserToken(long, java.lang.String)
     */
    public boolean saveUserToken(long id, String token) {
        Account a = (Account) read(id).getData();
        if (a == null) {
            return false;
        }
        a.setToken(token);
        accountDao.update(a);
        return true;
    }

    /** 
     * @see com.prj.service.AccountService#updateIcon(long, org.springframework.web.multipart.MultipartFile)
     */
    public DataWrapper updateIcon(long id, MultipartFile icon) {
        Account a = (Account) read(id).getData();
        if (a == null) {
            return new DataWrapper(ErrorCodeEnum.USER_NOT_FOUND, "用户不存在");
        }
        if (!icon.isEmpty()) {
            File iconFile = new File(RootPath.value + SystemConstants.UPLOAD_PATH + File.separator
                                     + icon.getName());
            try {
                icon.transferTo(iconFile);
            } catch (IllegalStateException e) {
                System.err.println("上传图片非法状态异常");
                e.printStackTrace();
                return new DataWrapper(ErrorCodeEnum.BIZ_ICON_UPLOAD, "非法状态");
            } catch (IOException e) {
                System.err.println("上传图片失败");
                e.printStackTrace();
                return new DataWrapper(ErrorCodeEnum.BIZ_ICON_UPLOAD, "转换文件失败");
            }
            a.setIconPath(iconFile.getAbsolutePath());
            accountDao.update(a);
        }
        return DataWrapper.voidSuccessRet;
    }

    /** 
     * @see com.prj.service.AccountService#sendCode(java.lang.String)
     */
    public DataWrapper sendCode(String mobile) {
        String code = CaptchaUtils.getRandomStr();
        if (CaptchaUtils.send(mobile, code)) {
            Captcha c = captchaDao.findBy("mobile", mobile);
            if (null == c) {
                c = new Captcha();
                c.setCode(code);
                c.setMobile(mobile);
                captchaDao.create(c);
            } else {
                c.setCode(code);
                c.setMobile(mobile);
                c.setExpires(System.currentTimeMillis() + SystemConstants.MSG_EXPIRE_INTERVAL);
                captchaDao.update(c);
            }
            return DataWrapper.voidSuccessRet;
        } else {
            return new DataWrapper(ErrorCodeEnum.BIZ_SEND_CODE, "验证码发送失败");
        }
    }

    /** 
     * @see com.prj.service.AccountService#verifyCode(java.lang.String, java.lang.String)
     */
    public DataWrapper verifyCode(String mobile, String code) {
        Captcha c = captchaDao.findBy("mobile", mobile);
        if (null != c && c.getCode().equals(code) && c.getExpires() > System.currentTimeMillis()) {
            return DataWrapper.voidSuccessRet;
        }
        return new DataWrapper(ErrorCodeEnum.BIZ_VERY_CODE, "验证码不正确");
    }

    /** 
     * @see com.prj.service.AccountService#verifyEmail(java.lang.String)
     */
    public DataWrapper verifyEmail(String email) {
        Account a = accountDao.findBy("email", email);
        if (null == a) {
            return DataWrapper.voidSuccessRet;
        }
        return new DataWrapper(ErrorCodeEnum.BIZ_DUPLICATIVE, "该邮箱已存在");
    }

}
