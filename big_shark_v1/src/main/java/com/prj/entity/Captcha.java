package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.prj.common.util.SystemConstants;

@Entity
@Table(name = "captcha")
public class Captcha extends BaseEntity {

    private String mobile;

    private String code;

    private long   expires;

    public Captcha() {
        this.expires = System.currentTimeMillis() + SystemConstants.MSG_EXPIRE_INTERVAL;
    }

    @Column(nullable = false)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(nullable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(nullable = false)
    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

}
