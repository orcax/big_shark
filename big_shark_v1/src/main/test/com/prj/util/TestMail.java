package com.prj.util;

import junit.framework.Assert;

import org.junit.Test;

import com.prj.common.util.Mail;
import com.prj.common.util.SystemConstants;

public class TestMail {

    @Test
    public void testMail() {
        String a = String.format(SystemConstants.EMAIL_TPL_REJECT, "hupeng");
        Mail mail = new Mail("qfavorite@qq.com", "test", a);
        Assert.assertTrue(mail.sendMail());
    }

    public static void main(String[] args) {
        String a = String.format(SystemConstants.EMAIL_TPL_WAIT, "胡鹏");
        Mail mail = new Mail("qfavorite@qq.com", "test", a);
        mail.sendMail();
        a = String.format(SystemConstants.EMAIL_TPL_REJECT, "胡鹏");
        mail = new Mail("qfavorite@qq.com", "test", a);
        mail.sendMail();
    }

}
