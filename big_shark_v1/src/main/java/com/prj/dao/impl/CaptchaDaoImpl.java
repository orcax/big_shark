package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.CaptchaDao;
import com.prj.entity.Captcha;

@Repository("CaptchaDaoImpl")
public class CaptchaDaoImpl extends BaseDaoHibernateImpl<Captcha, Long> implements CaptchaDao {

    public CaptchaDaoImpl() {
        super(Captcha.class);
    }
}
